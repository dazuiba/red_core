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
package com.aptana.editor.js.contentassist;

import beaver.Symbol;

import com.aptana.editor.js.parsing.ast.JSArrayNode;
import com.aptana.editor.js.parsing.ast.JSBinaryBooleanOperatorNode;
import com.aptana.editor.js.parsing.ast.JSBreakNode;
import com.aptana.editor.js.parsing.ast.JSCaseNode;
import com.aptana.editor.js.parsing.ast.JSCatchNode;
import com.aptana.editor.js.parsing.ast.JSConditionalNode;
import com.aptana.editor.js.parsing.ast.JSConstructNode;
import com.aptana.editor.js.parsing.ast.JSContinueNode;
import com.aptana.editor.js.parsing.ast.JSDeclarationNode;
import com.aptana.editor.js.parsing.ast.JSDefaultNode;
import com.aptana.editor.js.parsing.ast.JSDoNode;
import com.aptana.editor.js.parsing.ast.JSFalseNode;
import com.aptana.editor.js.parsing.ast.JSFinallyNode;
import com.aptana.editor.js.parsing.ast.JSForInNode;
import com.aptana.editor.js.parsing.ast.JSForNode;
import com.aptana.editor.js.parsing.ast.JSFunctionNode;
import com.aptana.editor.js.parsing.ast.JSGetElementNode;
import com.aptana.editor.js.parsing.ast.JSGetPropertyNode;
import com.aptana.editor.js.parsing.ast.JSGroupNode;
import com.aptana.editor.js.parsing.ast.JSIdentifierNode;
import com.aptana.editor.js.parsing.ast.JSIfNode;
import com.aptana.editor.js.parsing.ast.JSInvokeNode;
import com.aptana.editor.js.parsing.ast.JSLabelledNode;
import com.aptana.editor.js.parsing.ast.JSNameValuePairNode;
import com.aptana.editor.js.parsing.ast.JSNode;
import com.aptana.editor.js.parsing.ast.JSNullNode;
import com.aptana.editor.js.parsing.ast.JSNumberNode;
import com.aptana.editor.js.parsing.ast.JSObjectNode;
import com.aptana.editor.js.parsing.ast.JSParametersNode;
import com.aptana.editor.js.parsing.ast.JSParseRootNode;
import com.aptana.editor.js.parsing.ast.JSPostUnaryOperatorNode;
import com.aptana.editor.js.parsing.ast.JSPreUnaryOperatorNode;
import com.aptana.editor.js.parsing.ast.JSRegexNode;
import com.aptana.editor.js.parsing.ast.JSReturnNode;
import com.aptana.editor.js.parsing.ast.JSStatementsNode;
import com.aptana.editor.js.parsing.ast.JSStringNode;
import com.aptana.editor.js.parsing.ast.JSSwitchNode;
import com.aptana.editor.js.parsing.ast.JSThisNode;
import com.aptana.editor.js.parsing.ast.JSThrowNode;
import com.aptana.editor.js.parsing.ast.JSTreeWalker;
import com.aptana.editor.js.parsing.ast.JSTrueNode;
import com.aptana.editor.js.parsing.ast.JSTryNode;
import com.aptana.editor.js.parsing.ast.JSVarNode;
import com.aptana.editor.js.parsing.ast.JSWhileNode;
import com.aptana.editor.js.parsing.ast.JSWithNode;
import com.aptana.parsing.ast.IParseNode;

public class JSLocationIdentifier extends JSTreeWalker
{
	private int _offset;
	private LocationType _type;

	/**
	 * JSLocationWalker
	 */
	public JSLocationIdentifier(int offset)
	{
		offset--;

		this._offset = offset;
		this._type = LocationType.UNKNOWN;
	}

	/**
	 * getType
	 * 
	 * @return
	 */
	public LocationType getType()
	{
		return this._type;
	}

	/**
	 * setType
	 * 
	 * @param node
	 */
	protected void setType(IParseNode node)
	{
		if (node instanceof JSNode && node.contains(this._offset))
		{
			((JSNode) node).accept(this);
		}
	}

	/**
	 * setType
	 * 
	 * @param type
	 */
	protected void setType(LocationType type)
	{
		this._type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSArrayNode)
	 */
	@Override
	public void visit(JSArrayNode node)
	{
		if (node.contains(this._offset))
		{
			// TODO: Need to reconcile element-lists versus elision and need to
			// track left- and right-brackets
			this.setType(LocationType.IN_GLOBAL);
			this.visitChildren(node);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSBinaryBooleanOperatorNode)
	 */
	@Override
	public void visit(JSBinaryBooleanOperatorNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode lhs = node.getLeftHandSide();
			Symbol operator = node.getOperator();
			IParseNode rhs = node.getRightHandSide();

			if (lhs.contains(this._offset))
			{
				this.setType(lhs);
			}
			else if (operator != null && this._offset < operator.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (operator != null && this._offset < operator.getEnd())
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < rhs.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (rhs.contains(this._offset))
			{
				this.setType(rhs);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSBreakNode)
	 */
	@Override
	public void visit(JSBreakNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol label = node.getLabel();

			if (label != null && label.getStart() - 1 <= this._offset && this._offset <= label.getEnd())
			{
				this.setType(LocationType.IN_LABEL);
			}
			else if (this._offset == node.getEndingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSCaseNode)
	 */
	@Override
	public void visit(JSCaseNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol colon = node.getColon();

			if (this._offset == colon.getEnd())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset > colon.getEnd())
			{
				this.setType(LocationType.IN_GLOBAL);

				for (int i = 1; i < node.getChildCount(); i++)
				{
					IParseNode child = node.getChild(i);

					if (child.contains(this._offset))
					{
						this.setType(child);
						break;
					}
				}
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSCatchNode)
	 */
	@Override
	public void visit(JSCatchNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode body = node.getBody();

			if (body instanceof JSNode && body.contains(this._offset))
			{
				((JSNode) body).accept(this);
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSConditionalNode)
	 */
	@Override
	public void visit(JSConditionalNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.IN_GLOBAL);

			// partition by operators
			Symbol questionMark = node.getQuestionMark();
			Symbol colon = node.getColon();

			if (this._offset < questionMark.getStart())
			{
				this.setType(LocationType.NONE);
				this.setType(node.getTestExpression());
			}
			else if (this._offset == questionMark.getStart())
			{
				// done
			}
			else if (this._offset < colon.getStart())
			{
				this.setType(node.getTrueExpression());
			}
			else if (this._offset == colon.getStart())
			{
				// done
			}
			else
			{
				this.setType(node.getFalseExpression());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSConstructNode)
	 */
	@Override
	public void visit(JSConstructNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode identifier = node.getExpression();
			IParseNode arguments = node.getArguments();

			if (this._offset < node.getStart() + 3)
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < identifier.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset <= identifier.getEndingOffset())
			{
				this.setType(LocationType.IN_VARIABLE_NAME);
			}
			else if (this._offset <= arguments.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset < arguments.getEndingOffset())
			{
				this.setType(arguments);
			}
			else if (this._offset == arguments.getEndingOffset())
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSContinueNode)
	 */
	@Override
	public void visit(JSContinueNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol label = node.getLabel();

			if (label != null && label.getStart() - 1 <= this._offset && this._offset <= label.getEnd())
			{
				this.setType(LocationType.IN_LABEL);
			}
			else if (this._offset == node.getEndingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSDeclarationNode)
	 */
	@Override
	public void visit(JSDeclarationNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol equalSign = node.getEqualSign();

			if (equalSign != null)
			{
				IParseNode value = node.getValue();

				if (this._offset < equalSign.getStart())
				{
					this.setType(LocationType.NONE);
				}
				else if (this._offset < value.getStartingOffset())
				{
					this.setType(LocationType.IN_GLOBAL);
				}
				else
				{
					this.setType(value);
				}
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSDefaultNode)
	 */
	@Override
	public void visit(JSDefaultNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol colon = node.getColon();

			if (this._offset == colon.getEnd())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset > colon.getEnd())
			{
				this.setType(LocationType.IN_GLOBAL);

				for (int i = 0; i < node.getChildCount(); i++)
				{
					IParseNode child = node.getChild(i);

					if (child.contains(this._offset))
					{
						this.setType(child);
						break;
					}
				}
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSDoNode)
	 */
	@Override
	public void visit(JSDoNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode body = node.getBody();
			Symbol lparen = node.getLeftParenthesis();
			IParseNode condition = node.getCondition();
			Symbol rparen = node.getRightParenthesis();

			if (this._offset < body.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (body.contains(this._offset) && this._offset != body.getEndingOffset())
			{
				this.setType(body);
			}
			else if (this._offset < lparen.getStart())
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < condition.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (condition.contains(this._offset))
			{
				this.setType(condition);
			}
			else if (this._offset < rparen.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset == rparen.getStart())
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSFalseNode)
	 */
	@Override
	public void visit(JSFalseNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSFinallyNode)
	 */
	@Override
	public void visit(JSFinallyNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.NONE);

			this.visitChildren(node);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSForInNode)
	 */
	@Override
	public void visit(JSForInNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode initializer = node.getInitializer();
			Symbol in = node.getIn();
			IParseNode expression = node.getExpression();
			Symbol rightParen = node.getRightParenthesis();
			IParseNode body = node.getBody();

			if (this._offset < initializer.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (initializer.contains(this._offset) && this._offset != initializer.getEndingOffset())
			{
				this.setType(initializer);
			}
			else if (this._offset <= in.getEnd())
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < expression.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (expression.contains(this._offset))
			{
				this.setType(expression);
			}
			else if (this._offset < rightParen.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset < body.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (body.contains(this._offset) && this._offset != body.getEndingOffset())
			{
				this.setType(body);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSForNode)
	 */
	@Override
	public void visit(JSForNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode initializer = node.getInitializer();
			Symbol semi1 = node.getSemicolon1();
			IParseNode condition = node.getCondition();
			Symbol semi2 = node.getSemicolon2();
			IParseNode advance = node.getAdvance();
			Symbol rparen = node.getRightParenthesis();
			IParseNode body = node.getBody();

			if (this._offset < initializer.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (initializer.contains(this._offset))
			{
				if (this._offset == initializer.getEndingOffset())
				{
					this.setType(LocationType.NONE);
				}
				else
				{
					this.setType(initializer);
				}
			}
			else if (this._offset < semi1.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset < condition.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (condition.contains(this._offset))
			{
				if (this._offset == condition.getEndingOffset())
				{
					this.setType(LocationType.NONE);
				}
				else
				{
					this.setType(condition);
				}
			}
			else if (this._offset < semi2.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset < advance.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (advance.contains(this._offset))
			{
				this.setType(advance);
			}
			else if (this._offset < rparen.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset < body.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (body.contains(this._offset) && this._offset != body.getEndingOffset())
			{
				this.setType(body);
			}
			else
			{
				this.setType(LocationType.IN_GLOBAL);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSFunctionNode)
	 */
	@Override
	public void visit(JSFunctionNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode body = node.getBody();

			if (body.contains(this._offset))
			{
				this.setType(node.getBody());
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSGetElementNode)
	 */
	@Override
	public void visit(JSGetElementNode node)
	{
		this.visitChildren(node, node.getOperator());
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSGetPropertyNode)
	 */
	@Override
	public void visit(JSGetPropertyNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode lhs = node.getLeftHandSide();
			Symbol operator = node.getOperator();

			if (lhs.contains(this._offset) || lhs.getEndingOffset() <= this._offset && this._offset < operator.getStart())
			{
				this.setType(lhs);
			}
			else
			{
				this.setType(LocationType.IN_PROPERTY_NAME);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSGroupNode)
	 */
	@Override
	public void visit(JSGroupNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode expression = node.getExpression();
			Symbol rparen = node.getRightParenthesis();

			if (this._offset < expression.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (expression.contains(this._offset))
			{
				this.setType(expression);
			}
			else if (this._offset < rparen.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSIdentifierNode)
	 */
	@Override
	public void visit(JSIdentifierNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.IN_VARIABLE_NAME);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSIfNode)
	 */
	@Override
	public void visit(JSIfNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol lparen = node.getLeftParenthesis();
			IParseNode condition = node.getCondition();
			Symbol rparen = node.getRightParenthesis();
			IParseNode trueBlock = node.getTrueBlock();
			IParseNode falseBlock = node.getFalseBlock();

			if (this._offset < lparen.getStart())
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < condition.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (condition.contains(this._offset))
			{
				this.setType(condition);
			}
			else if (this._offset < rparen.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset < trueBlock.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (trueBlock.contains(this._offset) && this._offset != trueBlock.getEndingOffset())
			{
				this.setType(trueBlock);
			}
			else if (this._offset < falseBlock.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (falseBlock.contains(this._offset))
			{
				this.setType(falseBlock);
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSInvokeNode)
	 */
	@Override
	public void visit(JSInvokeNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode expression = node.getExpression();
			IParseNode arguments = node.getArguments();

			if (expression.contains(this._offset))
			{
				this.setType(expression);
			}
			else if (this._offset < arguments.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (arguments.contains(this._offset) && this._offset != arguments.getEndingOffset())
			{
				this.setType(arguments);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSLabelledNode)
	 */
	@Override
	public void visit(JSLabelledNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol colon = node.getColon();
			IParseNode block = node.getBlock();

			if (this._offset < colon.getStart())
			{
				this.setType(LocationType.IN_LABEL);
			}
			else if (this._offset < block.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (block.contains(this._offset))
			{
				this.setType(block);
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSNameValuePairNode)
	 */
	@Override
	public void visit(JSNameValuePairNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol colon = node.getColon();
			IParseNode value = node.getValue();

			if (this._offset < colon.getStart())
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < value.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (value.contains(this._offset))
			{
				this.setType(value);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSNullNode)
	 */
	@Override
	public void visit(JSNullNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSNumberNode)
	 */
	@Override
	public void visit(JSNumberNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSObjectNode)
	 */
	@Override
	public void visit(JSObjectNode node)
	{
		if (node.contains(this._offset))
		{
			// TODO: Need to track commas between name-value pairs
			this.setType(LocationType.NONE);
			this.visitChildren(node);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSParametersNode)
	 */
	@Override
	public void visit(JSParametersNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSParseRootNode)
	 */
	@Override
	public void visit(JSParseRootNode node)
	{
		this.setType(LocationType.IN_GLOBAL);

		if (node.contains(this._offset) && node.hasChildren())
		{
			for (IParseNode child : node)
			{
				if (child.contains(this._offset))
				{
					this.setType(child);

					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSPostUnaryOperatorNode)
	 */
	@Override
	public void visit(JSPostUnaryOperatorNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode expression = node.getExpression();
			Symbol operator = node.getOperator();

			if (expression.contains(this._offset))
			{
				this.setType(expression);
			}
			else if (this._offset < operator.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSPreUnaryOperatorNode)
	 */
	@Override
	public void visit(JSPreUnaryOperatorNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode expression = node.getExpression();
			Symbol operator = node.getOperator();

			if (this._offset < operator.getEnd())
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < expression.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (expression.contains(this._offset))
			{
				this.setType(expression);
			}
			else
			{
				this.setType(LocationType.NONE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSRegexNode)
	 */
	@Override
	public void visit(JSRegexNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSReturnNode)
	 */
	@Override
	public void visit(JSReturnNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode expression = node.getExpression();

			if (this._offset < expression.getStartingOffset())
			{
				if (this._offset + 1 == expression.getStartingOffset())
				{
					this.setType(LocationType.IN_GLOBAL);
				}
				else
				{
					this.setType(LocationType.NONE);
				}
			}
			else if (expression.contains(this._offset))
			{
				this.setType(expression);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSStatementsNode)
	 */
	@Override
	public void visit(JSStatementsNode node)
	{
		if (node.contains(this._offset) && node.getEndingOffset() != this._offset)
		{
			this.setType(LocationType.IN_GLOBAL);

			this.visitChildren(node);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSStringNode)
	 */
	@Override
	public void visit(JSStringNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSSwitchNode)
	 */
	@Override
	public void visit(JSSwitchNode node)
	{
		if (node.contains(this._offset) && this._offset != node.getEndingOffset())
		{
			Symbol lparen = node.getLeftParenthesis();
			IParseNode expression = node.getExpression();
			Symbol rparen = node.getRightParenthesis();
			Symbol lcurly = node.getLeftBrace();
			IParseNode firstStatement = node.getChild(1);
			IParseNode lastStatement = node.getLastChild();

			if (this._offset < lparen.getStart())
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < expression.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (expression.contains(this._offset))
			{
				this.setType(expression);
			}
			else if (this._offset < rparen.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset < lcurly.getStart())
			{
				this.setType(LocationType.NONE);
			}
			else if (firstStatement != null && this._offset < firstStatement.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (lastStatement != null && lastStatement.getEndingOffset() < this._offset)
			{
				this.setType(LocationType.NONE);
			}
			else
			{
				this.setType(LocationType.IN_GLOBAL);

				for (int i = 1; i < node.getChildCount(); i++)
				{
					IParseNode child = node.getChild(i);

					if (child.contains(this._offset))
					{
						this.setType(child);
						break;
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSThisNode)
	 */
	@Override
	public void visit(JSThisNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSThrowNode)
	 */
	@Override
	public void visit(JSThrowNode node)
	{
		if (node.contains(this._offset))
		{
			IParseNode expression = node.getExpression();

			if (this._offset < expression.getStartingOffset())
			{
				if (this._offset + 1 == expression.getStartingOffset())
				{
					this.setType(LocationType.IN_GLOBAL);
				}
				else
				{
					this.setType(LocationType.NONE);
				}
			}
			else if (expression.contains(this._offset))
			{
				this.setType(expression);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSTrueNode)
	 */
	@Override
	public void visit(JSTrueNode node)
	{
		if (node.contains(this._offset))
		{
			this.setType(LocationType.NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSTryNode)
	 */
	@Override
	public void visit(JSTryNode node)
	{
		if (node.contains(this._offset) && node.getEndingOffset() != this._offset)
		{
			this.setType(LocationType.NONE);

			this.visitChildren(node);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSVarNode)
	 */
	@Override
	public void visit(JSVarNode node)
	{
		if (node.contains(this._offset) && this._offset != node.getEndingOffset())
		{
			IParseNode firstDeclaration = node.getFirstChild();

			if (this._offset < firstDeclaration.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else
			{
				this.setType(LocationType.NONE);
				this.visitChildren(node);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSWhileNode)
	 */
	@Override
	public void visit(JSWhileNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol lparen = node.getLeftParenthesis();
			IParseNode condition = node.getCondition();
			Symbol rparen = node.getRightParenthesis();
			IParseNode body = node.getBody();

			if (this._offset < lparen.getStart())
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < condition.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (condition.contains(this._offset))
			{
				this.setType(condition);
			}
			else if (this._offset < rparen.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset < body.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (body.contains(this._offset))
			{
				this.setType(body);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visit(com.aptana.editor.js.parsing.ast.JSWithNode)
	 */
	@Override
	public void visit(JSWithNode node)
	{
		if (node.contains(this._offset))
		{
			Symbol lparen = node.getLeftParenthesis();
			IParseNode expression = node.getExpression();
			Symbol rparen = node.getRightParenthesis();
			IParseNode body = node.getBody();

			if (this._offset < lparen.getStart())
			{
				this.setType(LocationType.NONE);
			}
			else if (this._offset < expression.getStartingOffset())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (expression.contains(this._offset))
			{
				this.setType(expression);
			}
			else if (this._offset < rparen.getStart())
			{
				this.setType(LocationType.IN_GLOBAL);
			}
			else if (this._offset < body.getStartingOffset())
			{
				this.setType(LocationType.NONE);
			}
			else if (body.contains(this._offset))
			{
				this.setType(body);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.editor.js.parsing.ast.JSTreeWalker#visitChildren(com.aptana.editor.js.parsing.ast.JSNode)
	 */
	@Override
	protected void visitChildren(JSNode node)
	{
		if (node.contains(this._offset) && node.hasChildren())
		{
			for (IParseNode child : node)
			{
				if (child.contains(this._offset))
				{
					this.setType(child);

					break;
				}
			}
		}
	}

	/**
	 * visitChildren
	 * 
	 * @param node
	 * @param operator
	 */
	protected void visitChildren(JSNode node, Symbol operator)
	{
		this.visitChildren(node);

		if (this._type == LocationType.UNKNOWN)
		{
			if (operator != null)
			{
				if (operator.getStart() == this._offset + 1 || operator.getEnd() <= this._offset)
				{
					this.setType(LocationType.IN_GLOBAL);
				}
				else
				{
					this.setType(LocationType.NONE);
				}
			}
		}
	}
}
