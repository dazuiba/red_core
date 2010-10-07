package com.aptana.editor.dtd.parsing;

import java.util.ArrayList;
import com.aptana.editor.dtd.parsing.ast.*;
import com.aptana.parsing.ast.IParseRootNode;
import beaver.*;
import com.aptana.parsing.IParser;
import com.aptana.parsing.ast.IParseNode;
import com.aptana.parsing.IParseState;

/**
 * This class is a LALR parser generated by
 * <a href="http://beaver.sourceforge.net">Beaver</a> v0.9.6.1
 * from the grammar specification "DTD.grammar".
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DTDParser extends Parser implements IParser {

	static final ParsingTables PARSING_TABLES = new ParsingTables(
		"U9pDbWTqL4KKXl#tkub2eWar526qI0Xf1198I0YA0SJUKS4k5koze83qq5lelGG8FLYnObV" +
		"i1JiA8YWYAY2AvUT$inDvMJOIyNWExvrl$ZjtxjmtRPC1d0BCGX5MOYQMOmN6OGnwOmJcOJ" +
		"4MOX56emHBiGIhC0E3CGh568lXM83fc8g9c8zbc8RP68b9c8mfc83nc8EvbSuujD8P8t4CZ" +
		"iK0170KZaOLLCKbk1InyA5w#3OEmCssnH$IAxotE$$YzoWrRSUXBcv5ZCQTZykGX0X48Hgh" +
		"4OSKj49DDCGGj41Bd8jgg856E1MduNISWJDn5ix6EIIIJMHJr49jJwOwDjC1JuE3F3TPJp1" +
		"3SvoCM$xE5jGSgxbMEw3GZ3eHQSX29bgZ6tz05tH5UzoEOKX5EleY0stG11SZ3#rG7nVXVY" +
		"IW7df9cw6jz7ZrJqIy#cSYszY9niv40Kv0JvMTS06knOcu0htuCAxNl6$0GDo6at0NTw0nE" +
		"k8gTCOf#ifvE1yzS24knpMu4bVZHbo7Et477i3Tk0VteGyVmuFAq1$zy13kHV0P14VxwUvB" +
		"XFQeR6$SjN1d5wFThgRThg7rTrSrJgjPLpEehzrge1s9rtWQwzjDDPy4pQQvnj$YVyYOgbL" +
		"ErwfdQ9tRQ7Moj7hjz9qSVIbyMnUrXMy9xmreBSfoHNfEQPGzkJ7Q#IITlcH5HMW$t3aqD6" +
		"EFFcYqi94fsZsV9vFFPZhWoJl8KyjwoZBuvIcpAzBWg1sDD48ZZzGw#imwHgjthXcftvIEt" +
		"jgASTzOKK#x4AkJd2#DDgtkp0EASSmRCA#xc#rDwVRgg7DQm#nljDapHsq#lMvyPvrAl#cN" +
		"fx90#4rRJMcsD4vdscUzZlNvq9ZcKHImfb$mL0uKYrtxtw2dXnWiUjfwaTLbLakjRXQxnG2" +
		"nJKo01YGQYIQUV5#9lK7RyLjVOPZlRWofRofVTmBMlylMgrYjQZNAQbCHO#tgddnznVgGdC" +
		"r2nn4EnIM8vYBMrfDKxFc7#6GHAL9iFTLgIwjfeewrVQA1ZDLsd6kjlw6AxT8CMu#tcccrh" +
		"MWdiaIso95pfpJNjkTvnfFkdWwRFx4oSpxIqDDCv1zctzwLY76bVoNYrddiEVzXFiKXzHAh" +
		"AwoMUjesMDtYyPNxVHp6ztTuxAsLYDzug9YGU7jkqStZs#UnA$pz78W9$kOhoD$TQcb5CKS" +
		"gUjO4XN$oT#xZR$oLU$aBzt0tTt4DIsthp#8dyQFOYP27s$cTKVrzvPVyX1$p8sxWX$oMsx" +
		"YLtt0Bl#PcRbBh5#9pyPdOA3ur$JuGx$CzOx$3j$cMz4s#mVLydQ$nLRx2b$cI$TQBfdo1p" +
		"tCTt#LpVDRw7nMFY2TCxN7hNIrMMViPyRHuAcJqJugr$CFOAyKABkSoBk5YBc8n5t81vtCU" +
		"vt8EPtCMPt86ftCQft8A9tCIYpYH4pYUupYMOpYQepYI8pYSXHp6eHp2mHp4WLpA4gNletk" +
		"C8s1kEZcoMtd8g83M29IZao52#FMVsM$#EoSPV1xArrkgJmV5Tf2TB4rMFL#QhtgAObECbk" +
		"7e9kKN0TqFVCAz5VWz17GpS0v1mDmU2jJVlJbaMjhA7oH2jxGWxhqXMx7kRI9BRIwTpUtDC" +
		"QIfdgQuT6cwkQtvB8uXzmYcwtvXXpeO");

	private DTDScanner _scanner;
	
	/*
	 * (non-Javadoc)
	 * @see com.aptana.parsing.IParser#parse(com.aptana.parsing.IParseState)
	 */
	public synchronized IParseRootNode parse(IParseState parseState) throws java.lang.Exception
	{
		// grab source
		char[] characters = parseState.getSource();

		// make sure we have some source
		String source = (characters != null) ? new String(characters) : "";

		// send source to the scanner
		this._scanner.setSource(source);

		// parse
		IParseRootNode result = (IParseRootNode) parse(this._scanner);
		
		// save reference to result
		parseState.setParseResult(result);
		
		return result;
	}
	
	protected void addChildren(IParseNode node, ArrayList children)
	{
		if (node != null && children != null)
		{
			for (Object child : children)
			{
				if (child instanceof IParseNode)
				{
					node.addChild((IParseNode) child);
				}
			}
		}
	}

	public DTDParser() {
		super(PARSING_TABLES);


		this._scanner = new DTDScanner();
	}

	protected Symbol invokeReduceAction(int rule_num, int offset) {
		switch(rule_num) {
			case 0: // DTD = Declarations.d
			{
					final Symbol _symbol_d = _symbols[offset + 1];
					final ArrayList _list_d = (ArrayList) _symbol_d.value;
					final beaver.Symbol[] d = _list_d == null ? new beaver.Symbol[0] : (beaver.Symbol[]) _list_d.toArray(new beaver.Symbol[_list_d.size()]);
					
		return new DTDParseRootNode(d);
			}
			case 1: // DTD = 
			{
					
		return new DTDParseRootNode();
			}
			case 2: // Declarations = Declarations Declaration
			{
					((ArrayList) _symbols[offset + 1].value).add(_symbols[offset + 2]); return _symbols[offset + 1];
			}
			case 3: // Declarations = Declaration
			{
					ArrayList lst = new ArrayList(); lst.add(_symbols[offset + 1]); return new Symbol(lst);
			}
			case 11: // MarkupDecl = PI.p
			{
					final Symbol _symbol_p = _symbols[offset + 1];
					final String p = (String) _symbol_p.value;
					
		String content = p.substring(2, p.length() - 2);
		
		return new DTDProcessingInstructionNode(content);
			}
			case 13: // ElementDecl = ELEMENT NAME.n EMPTY GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
			DTDNode decl = new DTDElementDeclNode(n);
			
			decl.addChild(new DTDEmptyNode());
			
			return decl;
			}
			case 14: // ElementDecl = ELEMENT NAME.n ANY GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
			DTDNode decl = new DTDElementDeclNode(n);
			
			decl.addChild(new DTDAnyNode());
			
			return decl;
			}
			case 15: // ElementDecl = ELEMENT NAME.n Mixed.m GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					final Symbol _symbol_m = _symbols[offset + 3];
					final DTDNode m = (DTDNode) _symbol_m.value;
					
			DTDNode decl = new DTDElementDeclNode(n);
			
			decl.addChild(m);
			
			return decl;
			}
			case 16: // ElementDecl = ELEMENT NAME.n Children.c GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					final Symbol _symbol_c = _symbols[offset + 3];
					final DTDNode c = (DTDNode) _symbol_c.value;
					
			DTDNode decl = new DTDElementDeclNode(n);
			
			decl.addChild(c);
			
			return decl;
			}
			case 17: // Mixed = LPAREN PCDATA RPAREN STAR
			{
					
			DTDPCDataNode data = new DTDPCDataNode();
			DTDZeroOrMoreNode zom = new DTDZeroOrMoreNode();
			
			zom.addChild(data);
			
			return zom;
			}
			case 18: // Mixed = LPAREN PCDATA Names.n RPAREN STAR
			{
					final Symbol _symbol_n = _symbols[offset + 3];
					final ArrayList n = (ArrayList) _symbol_n.value;
					
			DTDPCDataNode data = new DTDPCDataNode();
			DTDZeroOrMoreNode zom = new DTDZeroOrMoreNode();
			
			zom.addChild(data);
			this.addChildren(zom, n);
			
			return zom;
			}
			case 19: // Mixed = LPAREN PCDATA RPAREN
			{
					
			return new DTDPCDataNode();
			}
			case 20: // Names = Names PIPE NAME
			{
					
			String name = (String) _symbols[offset + 3].value;
			DTDElementNode element = new DTDElementNode(name);
			((ArrayList) _symbols[offset + 1].value).add(element);
			
			return _symbols[offset + 1];
			}
			case 21: // Names = NAME
			{
					
			ArrayList lst = new ArrayList();
			String name = (String) _symbols[offset + 1].value;
			DTDElementNode element = new DTDElementNode(name);
			
			lst.add(element);
			
			return new Symbol(lst);
			}
			case 23: // Children = Choice.c QUESTION
			{
					final Symbol _symbol_c = _symbols[offset + 1];
					final DTDNode c = (DTDNode) _symbol_c.value;
					
			DTDOptionalNode result = new DTDOptionalNode();
			
			result.addChild(c);
			
			return result;
			}
			case 24: // Children = Choice.c STAR
			{
					final Symbol _symbol_c = _symbols[offset + 1];
					final DTDNode c = (DTDNode) _symbol_c.value;
					
			DTDZeroOrMoreNode result = new DTDZeroOrMoreNode();
			
			result.addChild(c);
			
			return result;
			}
			case 25: // Children = Choice.c PLUS
			{
					final Symbol _symbol_c = _symbols[offset + 1];
					final DTDNode c = (DTDNode) _symbol_c.value;
					
			DTDOneOrMoreNode result = new DTDOneOrMoreNode();
			
			result.addChild(c);
			
			return result;
			}
			case 27: // Children = Seq.s QUESTION
			{
					final Symbol _symbol_s = _symbols[offset + 1];
					final DTDNode s = (DTDNode) _symbol_s.value;
					
			DTDOptionalNode result = new DTDOptionalNode();
			
			result.addChild(s);
			
			return result;
			}
			case 28: // Children = Seq.s STAR
			{
					final Symbol _symbol_s = _symbols[offset + 1];
					final DTDNode s = (DTDNode) _symbol_s.value;
					
			DTDZeroOrMoreNode result = new DTDZeroOrMoreNode();
			
			result.addChild(s);
			
			return result;
			}
			case 29: // Children = Seq.s PLUS
			{
					final Symbol _symbol_s = _symbols[offset + 1];
					final DTDNode s = (DTDNode) _symbol_s.value;
					
			DTDOneOrMoreNode result = new DTDOneOrMoreNode();
			
			result.addChild(s);
			
			return result;
			}
			case 30: // Choice = LPAREN Cp.c ChoiceCps RPAREN
			{
					final Symbol _symbol_c = _symbols[offset + 2];
					final DTDNode c = (DTDNode) _symbol_c.value;
					
			ArrayList cps = (ArrayList) _symbols[offset + 3].value;
			DTDOrExpressionNode result = new DTDOrExpressionNode();

			// pre-pend leading cp			
			cps.add(0, c);
			
			// add all children to or-expr
			this.addChildren(result, cps);
			
			return result;
			}
			case 31: // ChoiceCps = ChoiceCps PIPE Cp.c
			{
					final Symbol _symbol_c = _symbols[offset + 3];
					final DTDNode c = (DTDNode) _symbol_c.value;
					
			((ArrayList) _symbols[offset + 1].value).add(c);
			
			return _symbols[offset + 1];
			}
			case 32: // ChoiceCps = PIPE Cp
			{
					
			ArrayList lst = new ArrayList();
			
			lst.add(_symbols[offset + 2]);
			
			return new Symbol(lst);
			}
			case 33: // Seq = LPAREN Cp.c RPAREN
			{
					final Symbol _symbol_c = _symbols[offset + 2];
					final DTDNode c = (DTDNode) _symbol_c.value;
					
			return c;
			}
			case 34: // Seq = LPAREN Cp.c SeqCps RPAREN
			{
					final Symbol _symbol_c = _symbols[offset + 2];
					final DTDNode c = (DTDNode) _symbol_c.value;
					
			ArrayList cps = (ArrayList) _symbols[offset + 3].value;
			DTDAndExpressionNode result = new DTDAndExpressionNode();

			// pre-pend leading cp			
			cps.add(0, c);
			
			// add all children to or-expr
			this.addChildren(result, cps);
			
			return result;
			}
			case 35: // SeqCps = SeqCps COMMA Cp.c
			{
					final Symbol _symbol_c = _symbols[offset + 3];
					final DTDNode c = (DTDNode) _symbol_c.value;
					
			((ArrayList) _symbols[offset + 1].value).add(c);
			
			return _symbols[offset + 1];
			}
			case 36: // SeqCps = COMMA Cp
			{
					
			ArrayList lst = new ArrayList();
			
			lst.add(_symbols[offset + 2]);
			
			return new Symbol(lst);
			}
			case 37: // Cp = NAME.n
			{
					final Symbol _symbol_n = _symbols[offset + 1];
					final String n = (String) _symbol_n.value;
					
			return new DTDElementNode(n);
			}
			case 38: // Cp = NAME.n QUESTION
			{
					final Symbol _symbol_n = _symbols[offset + 1];
					final String n = (String) _symbol_n.value;
					
			DTDOptionalNode node = new DTDOptionalNode();
			
			node.addChild(new DTDElementNode(n));
			}
			case 39: // Cp = NAME.n STAR
			{
					final Symbol _symbol_n = _symbols[offset + 1];
					final String n = (String) _symbol_n.value;
					
			DTDZeroOrMoreNode node = new DTDZeroOrMoreNode();
			
			node.addChild(new DTDElementNode(n));
			}
			case 40: // Cp = NAME.n PLUS
			{
					final Symbol _symbol_n = _symbols[offset + 1];
					final String n = (String) _symbol_n.value;
					
			DTDOneOrMoreNode node = new DTDOneOrMoreNode();
			
			node.addChild(new DTDElementNode(n));
			}
			case 42: // AttlistDecl = ATTLIST NAME.n GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
			return new DTDAttListDeclNode(n);
			}
			case 43: // AttlistDecl = ATTLIST NAME.n AttDefs GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
			Symbol _symbol_a = _symbols[offset + 3];
			ArrayList defs = (ArrayList) _symbol_a.value;
			DTDAttListDeclNode result = new DTDAttListDeclNode(n);
			
			this.addChildren(result, defs);
			
			return result;
			}
			case 44: // AttDefs = AttDefs AttDef
			{
					((ArrayList) _symbols[offset + 1].value).add(_symbols[offset + 2]); return _symbols[offset + 1];
			}
			case 45: // AttDefs = AttDef
			{
					ArrayList lst = new ArrayList(); lst.add(_symbols[offset + 1]); return new Symbol(lst);
			}
			case 46: // AttDef = NAME.n AttType.t REQUIRED.m
			{
					final Symbol _symbol_n = _symbols[offset + 1];
					final String n = (String) _symbol_n.value;
					final Symbol _symbol_t = _symbols[offset + 2];
					final DTDNode t = (DTDNode) _symbol_t.value;
					final Symbol _symbol_m = _symbols[offset + 3];
					final String m = (String) _symbol_m.value;
					
			return new DTDAttributeNode(n, t, m);
			}
			case 47: // AttDef = NAME.n AttType.t IMPLIED.m
			{
					final Symbol _symbol_n = _symbols[offset + 1];
					final String n = (String) _symbol_n.value;
					final Symbol _symbol_t = _symbols[offset + 2];
					final DTDNode t = (DTDNode) _symbol_t.value;
					final Symbol _symbol_m = _symbols[offset + 3];
					final String m = (String) _symbol_m.value;
					
			return new DTDAttributeNode(n, t, m);
			}
			case 48: // AttDef = NAME.n AttType.t STRING.m
			{
					final Symbol _symbol_n = _symbols[offset + 1];
					final String n = (String) _symbol_n.value;
					final Symbol _symbol_t = _symbols[offset + 2];
					final DTDNode t = (DTDNode) _symbol_t.value;
					final Symbol _symbol_m = _symbols[offset + 3];
					final String m = (String) _symbol_m.value;
					
			return new DTDAttributeNode(n, t, m.substring(1, m.length() - 1));
			}
			case 49: // AttDef = NAME.n AttType.t FIXED.m1 STRING.m2
			{
					final Symbol _symbol_n = _symbols[offset + 1];
					final String n = (String) _symbol_n.value;
					final Symbol _symbol_t = _symbols[offset + 2];
					final DTDNode t = (DTDNode) _symbol_t.value;
					final Symbol _symbol_m1 = _symbols[offset + 3];
					final String m1 = (String) _symbol_m1.value;
					final Symbol _symbol_m2 = _symbols[offset + 4];
					final String m2 = (String) _symbol_m2.value;
					
			return new DTDAttributeNode(n, t, m1 + " " + m2.substring(1, m2.length() - 1));
			}
			case 50: // AttType = CDATA_TYPE.t
			{
					final Symbol t = _symbols[offset + 1];
					
			return new DTDTypeNode((String) t.value);
			}
			case 51: // AttType = ID_TYPE.t
			{
					final Symbol t = _symbols[offset + 1];
					
			return new DTDTypeNode((String) t.value);
			}
			case 52: // AttType = IDREF_TYPE.t
			{
					final Symbol t = _symbols[offset + 1];
					
			return new DTDTypeNode((String) t.value);
			}
			case 53: // AttType = IDREFS_TYPE.t
			{
					final Symbol t = _symbols[offset + 1];
					
			return new DTDTypeNode((String) t.value);
			}
			case 54: // AttType = ENTITY_TYPE.t
			{
					final Symbol t = _symbols[offset + 1];
					
			return new DTDTypeNode((String) t.value);
			}
			case 55: // AttType = ENTITIES_TYPE.t
			{
					final Symbol t = _symbols[offset + 1];
					
			return new DTDTypeNode((String) t.value);
			}
			case 56: // AttType = NMTOKEN_TYPE.t
			{
					final Symbol t = _symbols[offset + 1];
					
			return new DTDTypeNode((String) t.value);
			}
			case 57: // AttType = NMTOKENS_TYPE.t
			{
					final Symbol t = _symbols[offset + 1];
					
			return new DTDTypeNode((String) t.value);
			}
			case 58: // AttType = NOTATION_TYPE LPAREN Names RPAREN
			{
					
			// TODO: add Names
			return new DTDNotationTypeNode();
			}
			case 59: // AttType = LPAREN NmTokens RPAREN
			{
					
			// TODO: add NmTokens
			return new DTDEnumerationTypeNode();
			}
			case 60: // NmTokens = NmTokens NAME
			{
					((ArrayList) _symbols[offset + 1].value).add(_symbols[offset + 2].value); return _symbols[offset + 1];
			}
			case 61: // NmTokens = NAME
			{
					ArrayList lst = new ArrayList(); lst.add(_symbols[offset + 1].value); return new Symbol(lst);
			}
			case 64: // GEDecl = ENTITY NAME.n STRING GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
			return new DTDGeneralEntityDeclNode(n);
			}
			case 65: // GEDecl = ENTITY NAME.n ExternalID GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
			return new DTDGeneralEntityDeclNode(n);
			}
			case 66: // GEDecl = ENTITY NAME.n ExternalID NDataDecl.nd GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					final Symbol _symbol_nd = _symbols[offset + 4];
					final DTDNode nd = (DTDNode) _symbol_nd.value;
					
			DTDGeneralEntityDeclNode result = new DTDGeneralEntityDeclNode(n);
			
			result.addChild(nd);
			
			return result;
			}
			case 67: // PEDecl = ENTITY PERCENT NAME.n STRING.s GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 3];
					final String n = (String) _symbol_n.value;
					final Symbol _symbol_s = _symbols[offset + 4];
					final String s = (String) _symbol_s.value;
					
			String value = s.substring(1, s.length() - 1);
			this._scanner.register(n, value);
			
			return new DTDParsedEntityDeclNode(n);
			}
			case 68: // PEDecl = ENTITY PERCENT NAME.n ExternalID GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 3];
					final String n = (String) _symbol_n.value;
					
			return new DTDParsedEntityDeclNode(n);
			}
			case 71: // NDataDecl = NDATA NAME.n
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
			return new DTDNDataDeclNode(n);
			}
			case 72: // NotationDecl = NOTATION NAME.n ExternalID GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
			return new DTDNotationDeclNode(n);
			}
			case 73: // NotationDecl = NOTATION NAME.n PublicID GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
			return new DTDNotationDeclNode(n);
			}
			case 77: // IncludeSect = SECTION_START INCLUDE LBRACKET SECTION_END
			{
					
 			return new DTDIncludeSectionNode();
			}
			case 78: // IncludeSect = SECTION_START INCLUDE LBRACKET Declarations.d SECTION_END
			{
					final Symbol _symbol_d = _symbols[offset + 4];
					final ArrayList _list_d = (ArrayList) _symbol_d.value;
					final beaver.Symbol[] d = _list_d == null ? new beaver.Symbol[0] : (beaver.Symbol[]) _list_d.toArray(new beaver.Symbol[_list_d.size()]);
					
 			DTDIncludeSectionNode result = new DTDIncludeSectionNode();
 			
 			for (Symbol s : d)
 			{
 				Object v = s.value;
 				
 				if (v instanceof IParseNode)
 				{
 					result.addChild((IParseNode) v);
 				}
 			}
 			
 			return result;
			}
			case 79: // IgnoreSect = SECTION_START IGNORE LBRACKET SECTION_END
			{
					
			return new DTDIgnoreSectionNode();
			}
			case 80: // IgnoreSect = SECTION_START IGNORE LBRACKET IgnoreSectionBody SECTION_END
			{
					
			return new DTDIgnoreSectionNode();
			}
			case 4: // Declaration = MarkupDecl
			case 5: // Declaration = PE_REF
			case 6: // Declaration = ConditionalSect
			case 7: // MarkupDecl = ElementDecl
			case 8: // MarkupDecl = AttlistDecl
			case 9: // MarkupDecl = EntityDecl
			case 10: // MarkupDecl = NotationDecl
			case 12: // MarkupDecl = COMMENT
			case 22: // Children = Choice
			case 26: // Children = Seq
			case 41: // Cp = Children
			case 62: // EntityDecl = GEDecl
			case 63: // EntityDecl = PEDecl
			case 75: // ConditionalSect = IncludeSect
			case 76: // ConditionalSect = IgnoreSect
			case 83: // IgnoreSectionBody = IgnoreToken
			case 84: // IgnoreSectionBody = ConditionalSect
			case 85: // IgnoreToken = ANY
			case 86: // IgnoreToken = ATTLIST
			case 87: // IgnoreToken = CDATA_TYPE
			case 88: // IgnoreToken = COMMA
			case 89: // IgnoreToken = COMMENT
			case 90: // IgnoreToken = ELEMENT
			case 91: // IgnoreToken = EMPTY
			case 92: // IgnoreToken = ENTITY
			case 93: // IgnoreToken = ENTITIES_TYPE
			case 94: // IgnoreToken = ENTITY_TYPE
			case 95: // IgnoreToken = FIXED
			case 96: // IgnoreToken = GREATER_THAN
			case 97: // IgnoreToken = ID_TYPE
			case 98: // IgnoreToken = IDREF_TYPE
			case 99: // IgnoreToken = IDREFS_TYPE
			case 100: // IgnoreToken = IGNORE
			case 101: // IgnoreToken = IMPLIED
			case 102: // IgnoreToken = INCLUDE
			case 103: // IgnoreToken = LBRACKET
			case 104: // IgnoreToken = LPAREN
			case 105: // IgnoreToken = NAME
			case 106: // IgnoreToken = NDATA
			case 107: // IgnoreToken = NMTOKEN_TYPE
			case 108: // IgnoreToken = NMTOKENS_TYPE
			case 109: // IgnoreToken = NOTATION
			case 110: // IgnoreToken = NOTATION_TYPE
			case 111: // IgnoreToken = PCDATA
			case 112: // IgnoreToken = PE_REF
			case 113: // IgnoreToken = PERCENT
			case 114: // IgnoreToken = PI
			case 115: // IgnoreToken = PIPE
			case 116: // IgnoreToken = PLUS
			case 117: // IgnoreToken = PUBLIC
			case 118: // IgnoreToken = QUESTION
			case 119: // IgnoreToken = REQUIRED
			case 120: // IgnoreToken = RPAREN
			case 121: // IgnoreToken = STAR
			case 122: // IgnoreToken = STRING
			case 123: // IgnoreToken = SYSTEM
			{
				return _symbols[offset + 1];
			}
			case 69: // ExternalID = SYSTEM STRING
			case 74: // PublicID = PUBLIC STRING
			case 81: // IgnoreSectionBody = IgnoreSectionBody IgnoreToken
			case 82: // IgnoreSectionBody = IgnoreSectionBody ConditionalSect
			{
				return _symbols[offset + 2];
			}
			case 70: // ExternalID = PUBLIC STRING STRING
			{
				return _symbols[offset + 3];
			}
			default:
				throw new IllegalArgumentException("unknown production #" + rule_num);
		}
	}
}