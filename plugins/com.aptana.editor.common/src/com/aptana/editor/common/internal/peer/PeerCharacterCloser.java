/**
 * This file Copyright (c) 2005-2010 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or modify this
 * program under the terms of the GNU General Public License,
 * Version 3, as published by the Free Software Foundation.  You should
 * have received a copy of the GNU General Public License, Version 3 along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Aptana provides a special exception to allow redistribution of this file
 * with certain other free and open source software ("FOSS") code and certain additional terms
 * pursuant to Section 7 of the GPL. You may view the exception and these
 * terms on the web at http://www.aptana.com/legal/gpl/.
 * 
 * 2. For the Aptana Public License (APL), this program and the
 * accompanying materials are made available under the terms of the APL
 * v1.0 which accompanies this distribution, and is available at
 * http://www.aptana.com/legal/apl/.
 * 
 * You may view the GPL, Aptana's exception and additional terms, and the
 * APL in the file titled license.html at the root of the corresponding
 * plugin containing this source file.
 * 
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.common.internal.peer;

import java.util.Stack;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPartitioningException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.link.ILinkedModeListener;
import org.eclipse.jface.text.link.LinkedModeModel;
import org.eclipse.jface.text.link.LinkedModeUI;
import org.eclipse.jface.text.link.LinkedPosition;
import org.eclipse.jface.text.link.LinkedPositionGroup;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.texteditor.link.EditorLinkedModeUI;

import com.aptana.editor.common.CommonEditorPlugin;
import com.aptana.scope.ScopeSelector;

/**
 * A class that can be installed on a ITextViewer and will auto-insert the closing peer character for typical paired
 * characters like (), [], {}, '', "", ``, <>. This class will wrap selected text in the pair if there's a selected
 * length > 0. If the length is 0, it will only insert the pair if the following character is not a closing char. When
 * inserting the pair the editor will enter linked mode, so that if user types close character manually they will just
 * "overwrite" the smart close char we inserted.
 * 
 * @author cwilliams
 */
public class PeerCharacterCloser implements VerifyKeyListener, ILinkedModeListener
{

	private ITextViewer textViewer;
	private final String CATEGORY = toString();
	private IPositionUpdater fUpdater = new ExclusivePositionUpdater(CATEGORY);
	private Stack<BracketLevel> fBracketLevelStack = new Stack<BracketLevel>();
	private char[] pairs;

	private static final ScopeSelector fgCommentSelector = new ScopeSelector("comment"); //$NON-NLS-1$

	PeerCharacterCloser(ITextViewer textViewer, char[] pairs)
	{
		this.textViewer = textViewer;
		this.pairs = pairs;
	}

	public static PeerCharacterCloser install(ITextViewer textViewer, char[] pairs)
	{
		PeerCharacterCloser pairMatcher = new PeerCharacterCloser(textViewer, pairs);
		textViewer.getTextWidget().addVerifyKeyListener(pairMatcher);
		return pairMatcher;
	}

	/**
	 * @see org.eclipse.swt.custom.VerifyKeyListener#verifyKey(org.eclipse.swt.events.VerifyEvent)
	 */
	public void verifyKey(VerifyEvent event)
	{
		// early pruning to slow down normal typing as little as possible
		if (!event.doit || !isAutoInsertEnabled() || !isAutoInsertCharacter(event.character))
		{
			return;
		}

		IDocument document = textViewer.getDocument();
		final Point selection = textViewer.getSelectedRange();
		final int offset = selection.x;
		final int length = selection.y;

		try
		{

			String scope = getScopeAtOffset(document, offset);
			if (fgCommentSelector.matches(scope))
			{
				return;
			}

			if (length > 0)
			{
				wrapSelection(event, document, offset, length);
				return;
			}

			// Don't auto-close if next char is a letter or digit
			if (document.getLength() > offset)
			{
				char nextChar = document.getChar(offset);
				if (Character.isJavaIdentifierPart(nextChar))
				{
					return;
				}
			}

			// Don't auto-close if we have an open pair!
			if (isUnclosedPair(event, document, offset)) // We have an open string or pair, just insert the single
															// character, don't do anything special
			{
				return;
			}

			final char closingCharacter = getPeerCharacter(event.character);
			// If this is the start char and there's no unmatched close char, insert the close char
			if (unpairedClose(event.character, closingCharacter, document, offset))
			{
				return;
			}

			final StringBuffer buffer = new StringBuffer();
			buffer.append(event.character);
			buffer.append(closingCharacter);
			if (offset == document.getLength())
			{
				String delim = null;
				if (document instanceof IDocumentExtension4)
				{
					delim = ((IDocumentExtension4) document).getDefaultLineDelimiter();
				}
				if (delim == null)
				{
					delim = System.getProperty("line.separator", "\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				buffer.append(delim);
			}

			document.replace(offset, length, buffer.toString());

			BracketLevel level = new BracketLevel();
			fBracketLevelStack.push(level);

			LinkedPositionGroup group = new LinkedPositionGroup();
			group.addPosition(new LinkedPosition(document, offset + 1, 0, LinkedPositionGroup.NO_STOP));

			LinkedModeModel model = new LinkedModeModel();
			model.addLinkingListener(this);
			model.addGroup(group);
			model.forceInstall();

			// set up position tracking for our magic peers
			if (fBracketLevelStack.size() == 1)
			{
				document.addPositionCategory(CATEGORY);
				document.addPositionUpdater(fUpdater);
			}
			level.fFirstPosition = new Position(offset, 1);
			level.fSecondPosition = new Position(offset + 1, 1);
			document.addPosition(CATEGORY, level.fFirstPosition);
			document.addPosition(CATEGORY, level.fSecondPosition);

			level.fUI = new EditorLinkedModeUI(model, textViewer);
			level.fUI.setSimpleMode(true);
			level.fUI.setExitPolicy(new ExitPolicy(textViewer, closingCharacter, getEscapeCharacter(closingCharacter),
					fBracketLevelStack));
			level.fUI.setExitPosition(textViewer, offset + 2, 0, Integer.MAX_VALUE);
			level.fUI.setCyclingMode(LinkedModeUI.CYCLE_NEVER);
			level.fUI.enter();

			IRegion newSelection = level.fUI.getSelectedRegion();
			textViewer.setSelectedRange(newSelection.getOffset(), newSelection.getLength());

			event.doit = false;
		}
		catch (BadLocationException e)
		{
			CommonEditorPlugin.logError(e);
		}
		catch (BadPositionCategoryException e)
		{
			CommonEditorPlugin.logError(e);
		}
	}

	protected String getScopeAtOffset(IDocument document, final int offset) throws BadLocationException
	{
		return CommonEditorPlugin.getDefault().getDocumentScopeManager().getScopeAtOffset(document, offset);
	}

	boolean unpairedClose(char openingChar, char closingCharacter, IDocument document, int offset)
	{
		try
		{
			// Now we need to do smarter checks, see if rest of doc contains unbalanced set!
			String before = document.get(0, offset); // don't cheat and trim because we need offsets to match for
														// comment scope matching
			int stackLevel = 0;
			for (int i = 0; i < before.length(); i++)
			{

				char c = before.charAt(i);
				if (c == openingChar && openingChar == closingCharacter)
				{
					if (!fgCommentSelector.matches(getScopeAtOffset(document, i)))
					{
						stackLevel++;
						stackLevel = stackLevel % 2;
					}
				}
				else if (c == openingChar)
				{
					if (!fgCommentSelector.matches(getScopeAtOffset(document, i)))
					{
						stackLevel++;
					}
				}
				else if (c == closingCharacter)
				{
					if (!fgCommentSelector.matches(getScopeAtOffset(document, i)))
					{
						stackLevel--;
					}
				}
			}

			String after = document.get(offset, document.getLength() - offset); // don't cheat and trim because we need
																				// offsets to match for comment scope
																				// matching
			for (int i = 0; i < after.length(); i++)
			{
				char c = after.charAt(i);
				if (c == openingChar && openingChar == closingCharacter)
				{
					if (!fgCommentSelector.matches(getScopeAtOffset(document, offset + i)))
					{
						stackLevel++;
						stackLevel = stackLevel % 2;
					}
				}
				else if (c == openingChar)
				{
					if (!fgCommentSelector.matches(getScopeAtOffset(document, offset + i)))
					{
						stackLevel++;
					}
				}
				else if (c == closingCharacter)
				{
					if (!fgCommentSelector.matches(getScopeAtOffset(document, offset + i)))
					{
						stackLevel--;
						if (stackLevel < 0)
							return true;
					}
				}
			}
			return stackLevel != 0;
		}
		catch (BadLocationException e)
		{
			// ignore
		}
		return false;
	}

	private boolean isUnclosedPair(VerifyEvent event, IDocument document, int offset) throws BadLocationException
	{
		final char closingCharacter = getPeerCharacter(event.character);
		// This doesn't matter if the user is not typing an "end" character.
		if (closingCharacter != event.character)
			return false;
		char c = event.character;
		int beginning = 0;
		// Don't check from very beginning of the document! Be smarter/quicker and check from beginning of
		// partition if we can
		if (document instanceof IDocumentExtension3)
		{
			try
			{
				IDocumentExtension3 ext = (IDocumentExtension3) document;
				ITypedRegion region = ext.getPartition(IDocumentExtension3.DEFAULT_PARTITIONING, offset, false);
				beginning = region.getOffset();
			}
			catch (BadPartitioningException e)
			{
				// ignore
			}
		}
		// Now check leading source and see if we're an unclosed pair.
		String previous = document.get(beginning, offset - beginning);
		boolean open = false;
		int index = -1;
		while ((index = previous.indexOf(c, index + 1)) != -1)
		{
			if (fgCommentSelector.matches(getScopeAtOffset(document, beginning + index)))
				continue;
			open = !open;
			if (open)
			{
				c = closingCharacter;
			}
			else
			{
				c = event.character;
			}
		}
		return open;
	}

	/**
	 * Return the character which escapes the closing character passed in. '\' for string chars ('"', ''')
	 * 
	 * @param character
	 * @return
	 */
	private static char getEscapeCharacter(char character)
	{
		switch (character)
		{
			case '"':
			case '\'':
				return '\\';
			default:
				return 0;
		}
	}

	/**
	 * Wraps the selected text in the smart pair.
	 * 
	 * @param event
	 * @param document
	 * @param offset
	 * @param length
	 * @throws BadLocationException
	 */
	private void wrapSelection(VerifyEvent event, IDocument document, final int offset, final int length)
			throws BadLocationException
	{
		final char closingCharacter = getPeerCharacter(event.character);
		final StringBuffer buffer = new StringBuffer();
		buffer.append(event.character);
		buffer.append(document.get(offset, length));
		buffer.append(closingCharacter);
		document.replace(offset, length, buffer.toString());
		event.doit = false;
	}

	/**
	 * Return the closing character of the pair.
	 * 
	 * @param character
	 * @return
	 */
	private char getPeerCharacter(char character)
	{
		for (int i = 0; i < pairs.length; i += 2)
		{
			if (pairs[i] == character)
			{
				return pairs[i + 1];
			}
		}
		return character;
	}

	/**
	 * Is the character typed one of the ones we do smart pairing of?
	 * 
	 * @param character
	 * @return
	 */
	private boolean isAutoInsertCharacter(char character)
	{
		for (int i = 0; i < pairs.length; i += 2)
		{
			if (pairs[i] == character)
			{
				return true;
			}
		}
		return false;
	}

	private boolean isAutoInsertEnabled()
	{
		// TODO Set up a pref to turn this on or off
		return true;
	}


	/**
	 * Simple class to hold linked mode and two positions.
	 * 
	 * @author cwilliams
	 */
	static class BracketLevel
	{
		LinkedModeUI fUI;
		Position fFirstPosition;
		Position fSecondPosition;
	}

	/**
	 * Position updater that takes any changes at the borders of a position to not belong to the position.
	 */
	private static class ExclusivePositionUpdater implements IPositionUpdater
	{

		/** The position category. */
		private final String fCategory;

		/**
		 * Creates a new updater for the given <code>category</code>.
		 * 
		 * @param category
		 *            the new category.
		 */
		public ExclusivePositionUpdater(String category)
		{
			fCategory = category;
		}

		/*
		 * @see org.eclipse.jface.text.IPositionUpdater#update(org.eclipse.jface.text.DocumentEvent)
		 */
		public void update(DocumentEvent event)
		{

			int eventOffset = event.getOffset();
			int eventOldLength = event.getLength();
			int eventNewLength = event.getText() == null ? 0 : event.getText().length();
			int deltaLength = eventNewLength - eventOldLength;

			try
			{
				Position[] positions = event.getDocument().getPositions(fCategory);

				for (int i = 0; i != positions.length; i++)
				{

					Position position = positions[i];

					if (position.isDeleted())
						continue;

					int offset = position.getOffset();
					int length = position.getLength();
					int end = offset + length;

					if (offset >= eventOffset + eventOldLength)
						// position comes
						// after change - shift
						position.setOffset(offset + deltaLength);
					else if (end <= eventOffset)
					{
						// position comes way before change -
						// leave alone
					}
					else if (offset <= eventOffset && end >= eventOffset + eventOldLength)
					{
						// event completely internal to the position - adjust
						// length
						position.setLength(length + deltaLength);
					}
					else if (offset < eventOffset)
					{
						// event extends over end of position - adjust length
						int newEnd = eventOffset;
						position.setLength(newEnd - offset);
					}
					else if (end > eventOffset + eventOldLength)
					{
						// event extends from before position into it - adjust
						// offset
						// and length
						// offset becomes end of event, length ajusted
						// acordingly
						int newOffset = eventOffset + eventNewLength;
						position.setOffset(newOffset);
						position.setLength(end - newOffset);
					}
					else
					{
						// event consumes the position - delete it
						position.delete();
					}
				}
			}
			catch (BadPositionCategoryException e)
			{
				// ignore and return
			}
		}
	}

	/*
	 * @see org.eclipse.jface.text.link.ILinkedModeListener#left(org.eclipse.jface.text.link.LinkedModeModel, int)
	 */
	public void left(LinkedModeModel environment, int flags)
	{

		final BracketLevel level = fBracketLevelStack.pop();

		if (flags != ILinkedModeListener.EXTERNAL_MODIFICATION)
			return;

		// remove brackets
		final IDocument document = textViewer.getDocument();
		if (document instanceof IDocumentExtension)
		{
			IDocumentExtension extension = (IDocumentExtension) document;
			extension.registerPostNotificationReplace(null, new IDocumentExtension.IReplace()
			{

				public void perform(IDocument d, IDocumentListener owner)
				{
					if ((level.fFirstPosition.isDeleted || level.fFirstPosition.length == 0)
							&& !level.fSecondPosition.isDeleted
							&& level.fSecondPosition.offset == level.fFirstPosition.offset)
					{
						try
						{
							document.replace(level.fSecondPosition.offset, level.fSecondPosition.length, null);
						}
						catch (BadLocationException e)
						{
							CommonEditorPlugin.logError(e);
						}
					}

					if (fBracketLevelStack.size() == 0)
					{
						document.removePositionUpdater(fUpdater);
						try
						{
							document.removePositionCategory(CATEGORY);
						}
						catch (BadPositionCategoryException e)
						{
							CommonEditorPlugin.logError(e);
						}
					}
				}
			});
		}
	}

	/*
	 * @see org.eclipse.jface.text.link.ILinkedModeListener#suspend(org.eclipse.jface.text.link.LinkedModeModel)
	 */
	public void suspend(LinkedModeModel environment)
	{
	}

	/*
	 * @see org.eclipse.jface.text.link.ILinkedModeListener#resume(org.eclipse.jface.text.link.LinkedModeModel, int)
	 */
	public void resume(LinkedModeModel environment, int flags)
	{
	}
}
