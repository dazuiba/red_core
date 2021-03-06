package com.aptana.editor.js.contentassist;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

import com.aptana.editor.common.AbstractThemeableEditor;
import com.aptana.editor.common.contentassist.CommonCompletionProposal;
import com.aptana.editor.js.tests.EditorBasedTests;
import com.aptana.editor.js.tests.TextViewer;
import com.aptana.parsing.lexer.Range;

public class RangeTests extends EditorBasedTests
{
	static class OffsetSelection
	{
		public final int startingOffset;
		public final int endingOffset;
		public final Range range;

		public OffsetSelection(int offset, Range range)
		{
			this.startingOffset = offset;
			this.endingOffset = offset;
			this.range = range;
		}
		
		public OffsetSelection(int startingOffset, int endingOffset, Range range)
		{
			this.startingOffset = startingOffset;
			this.endingOffset = endingOffset;
			this.range = range;
		}
	}

	/**
	 * rangeTests
	 * 
	 * @param resource
	 * @param selections
	 */
	protected void rangeTests(String resource, OffsetSelection... selections)
	{
		TestContext context = this.getTestContext(resource);
		ITextViewer viewer = new TextViewer(context.document);
		JSContentAssistProcessor processor = new JSContentAssistProcessor((AbstractThemeableEditor) context.editor);

		for (OffsetSelection selection : selections)
		{
			for (int offset = selection.startingOffset; offset <= selection.endingOffset; offset++)
			{
				ICompletionProposal[] proposals = processor.computeCompletionProposals(viewer, offset, '\0', false);
	
				if (proposals != null && proposals.length > 0)
				{
					if (selection.range != null)
					{
						CommonCompletionProposal proposal = (CommonCompletionProposal) proposals[0];
						
						if (selection.range.isEmpty())
						{
							assertEquals(offset, proposal.getReplaceRange().getStartingOffset());
						}
						else
						{
							assertEquals("offset " + offset, selection.range, proposal.getReplaceRange());
						}
					}
					else
					{
						fail("Unexpected proposals at offset " + offset);
					}
				}
				else
				{
					if (selection.range != null)
					{
						fail("No proposals at offset " + offset);
					}
				}
			}
		}
	}
	
	/**
	 * testFunctionWithoutArgs
	 */
	public void testFunctionWithoutArgs()
	{
		this.rangeTests(
			"ranges/functionWithoutArgs.js",
			new OffsetSelection(0, 15, null),
			new OffsetSelection(16, 17, Range.EMPTY)
		);
	}
	
	/**
	 * testFunctionWithArgs
	 */
	public void testFunctionWithArgs()
	{
		this.rangeTests(
			"ranges/functionWithArgs.js",
			new OffsetSelection(0, 24, null),
			new OffsetSelection(25, 26, Range.EMPTY)
		);
	}
	
	/**
	 * testInvokeWithoutParams
	 */
	public void testInvokeWithoutParams()
	{
		this.rangeTests(
			"ranges/invokeWithoutParams.js",
			new OffsetSelection(0, Range.EMPTY),
			new OffsetSelection(1, 3, new Range(0, 2)),
			new OffsetSelection(4, 5, Range.EMPTY)
		);
	}
	
	/**
	 * testInvokeWithParams
	 */
	public void testInvokeWithParams()
	{
		this.rangeTests(
			"ranges/invokeWithParams.js",
			new OffsetSelection(0, Range.EMPTY),
			new OffsetSelection(1, 3, new Range(0, 2)),
			new OffsetSelection(4, Range.EMPTY),
			new OffsetSelection(5, new Range(4)),
			new OffsetSelection(6, Range.EMPTY),
			new OffsetSelection(7, 8, new Range(6, 7)),
			new OffsetSelection(9, 10, Range.EMPTY),
			new OffsetSelection(11, 13, new Range(10, 12)),
			new OffsetSelection(14, Range.EMPTY)
		);
	}
	
	/**
	 * testIfStatement
	 */
	public void testIfStatement()
	{
		this.rangeTests(
			"ranges/ifStatement.js",
			new OffsetSelection(0, 3, null),
			new OffsetSelection(4, Range.EMPTY),
			new OffsetSelection(5, new Range(4)),
			new OffsetSelection(6, 7, null),
			new OffsetSelection(8, Range.EMPTY),
			new OffsetSelection(9, 15, null),
			new OffsetSelection(16, 17, Range.EMPTY)
		);
	}
	
	/**
	 * testWhileStatement
	 */
	public void testWhileStatement()
	{
		this.rangeTests(
			"ranges/whileStatement.js",
			new OffsetSelection(0, 6, null),
			new OffsetSelection(7, Range.EMPTY),
			new OffsetSelection(8, new Range(7)),
			new OffsetSelection(9, 10, null),
			new OffsetSelection(11, 12, Range.EMPTY)
		);
	}
	
	/**
	 * testForStatement
	 */
	public void testForStatement()
	{
		this.rangeTests(
			"ranges/forStatement.js",
			new OffsetSelection(0, 29, null),
			new OffsetSelection(30, 31, Range.EMPTY)
		);
	}
	
	/**
	 * testForInStatement
	 */
	public void testForInStatement()
	{
		this.rangeTests(
			"ranges/forInStatement.js",
			new OffsetSelection(0, 19, null),
			new OffsetSelection(20, 21, Range.EMPTY)
		);
	}
}
