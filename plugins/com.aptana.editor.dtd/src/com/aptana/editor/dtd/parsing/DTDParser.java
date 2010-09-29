package com.aptana.editor.dtd.parsing;

import java.util.ArrayList;
import com.aptana.editor.dtd.parsing.ast.*;
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
		"U9oTbMTqL4KOXj#xkHiHY1enM21WB90i8KgowI6QiDc0NK15ewBObQ1H81Q4WA8Som$zu$4" +
		"S#m4KAO8YLK1G4RkYO2$OU##Kr$VEdJrUrdLPkFSyNxlVp3SpTyuCW7Mu4UV0HJPsGoViZe" +
		"jmCIv026SZ2yr9yJw881$7uNYSW5Dn6gVpFRIWAxUX2dMeHWrgSIA6OXY6uoISZ5Cm0kFH1" +
		"KVY4X9XTEPRoC5voCNUE1e3KOyZq0td9tBGtSl1GIZ1VZW0Xw47eZZ6L2t041o5FR0lziHU" +
		"oCF1E0I7eWBxe2yAKOH#A4P$EpvFbXbPRcGbhiPKzCBX6819UlT7czxUQCM1fbQGKYCR4SU" +
		"5Y4cUYKirYhFuiKRKZa6u0jVmHRJn6zo0SVoQN$5BRk97$11DwCbt#GxVncI#oOtSWC6u4W" +
		"qu0yVYMbo7qp4Ivs8qnk0oJC0eN8NBCPOlyGLqO28NutgCmnG#p#Sq5rTpAT7ylH6NQYvTD" +
		"PCinIkJulxS6pMkcEh7LT7H2eK0yyN$RxqromBDob#nFbf3RmLpbTDDg#VelxdcFuHjVfQg" +
		"9NAY9YUaMBQolBsGhTNpg#PhHHtznnxeAUctpTCg#POl$Sp$oh1URw#u#jkTrN#ENbT$Ajl" +
		"K2f9df2DPRAIhNUEDnJ6v5MeTaPqZ8jf3Jk0jrdVVwcvrbVPOI6zOUwj1lKQrl$sSQitPKV" +
		"kmyS8ssiLgO5e7p5CWoZmx7NfwYJentFftYhl4tU8U4HVDubuH4$U9yJRtVl60jGUBTcjFi" +
		"7fOKgqHINvBaZ$HwZQhEwoUOlLKg$F5T353p1JbuY4nIpmivWJMuUP0$vr4HOerc9qSonGz" +
		"yyLSSPFrRxLw7icjrewcQRzGt28MMN#nrGkiNY9ki$RjOhHOPlqLuWwnKgmIgyLoiLGy9Lh" +
		"56flxZ5YRgAknRH2RCfcZyZQd#VQzLEwE#j1J7R2BCgcRee$w9B$1wfZLpJlHrqYhsmDpsI" +
		"WrTbV6ba6zZXInCHcs7ReJTLftL3VmhUr$ujDynQtSpB$v5$$a7$oTlzdehqZny0CZtpTod" +
		"NYMY$Wu5$8nFie5dCz7E8zpEOUpzKrxaKyBxL6k5MjIzlYB#9a$6ViBliPN#Ogib$a6N#Tw" +
		"hkQJNCMLNC4dk9pBk9HBR3kT09nbB9q9V90pEOC$yWUpKxteT#9RyQdnFh7HprEEuJCZjqY" +
		"qc1lBE#NATTQMo#vdwP#24hYw#xA4gvFVE$qTtOG#0nJpg3CtshyKAxzKyJATwX6rYSWVA5" +
		"qlNG$NwAWXf3lG4Q40s$kLyXDKRKT8DujZ2UbkSGnzrN#D#gvHh52wK7wjTAtyHdqVb8QOf" +
		"LgvHT95HYT8xpUfJfFc6rUjk6plbcjMpf1$02bgba8=");

	private DTDScanner _scanner;
	
	/*
	 * (non-Javadoc)
	 * @see com.aptana.parsing.IParser#parse(com.aptana.parsing.IParseState)
	 */
	@Override
	public synchronized IParseNode parse(IParseState parseState) throws java.lang.Exception
	{
		// grab source
		char[] characters = parseState.getSource();

		// make sure we have some source
		String source = (characters != null) ? new String(characters) : "";

		// send source to the scanner
		this._scanner.setSource(source);

		// parse
		IParseNode result = (IParseNode) parse(this._scanner);
		
		// save reference to result
		parseState.setParseResult(result);
		
		return result;
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
			case 10: // MarkupDecl = PI.p
			{
					final Symbol _symbol_p = _symbols[offset + 1];
					final String p = (String) _symbol_p.value;
					
		String content = p.substring(2, p.length() - 2);
		
		return new DTDProcessingInstructionNode(content);
			}
			case 11: // ElementDecl = ELEMENT NAME.n ContentSpec GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
		return new DTDElementDeclarationNode(n);
			}
			case 12: // AttlistDecl = ATTLIST NAME.n GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
		return new DTDAttributeListDeclarationNode(n);
			}
			case 13: // AttlistDecl = ATTLIST NAME.n AttDefs GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
		return new DTDAttributeListDeclarationNode(n);
			}
			case 14: // AttDefs = AttDefs AttDef
			{
					((ArrayList) _symbols[offset + 1].value).add(_symbols[offset + 2]); return _symbols[offset + 1];
			}
			case 15: // AttDefs = AttDef
			{
					ArrayList lst = new ArrayList(); lst.add(_symbols[offset + 1]); return new Symbol(lst);
			}
			case 18: // NotationDecl = NOTATION NAME.n ExternalID GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
		return new DTDNotationDeclarationNode(n);
			}
			case 19: // NotationDecl = NOTATION NAME.n PublicID GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
		return new DTDNotationDeclarationNode(n);
			}
			case 25: // GEDecl = ENTITY NAME.n EntityDef GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 2];
					final String n = (String) _symbol_n.value;
					
		return new DTDGEntityDeclarationNode(n);
			}
			case 26: // PEDecl = ENTITY PERCENT NAME.n PEDef GREATER_THAN
			{
					final Symbol _symbol_n = _symbols[offset + 3];
					final String n = (String) _symbol_n.value;
					
		return new DTDPEntityDeclarationNode(n);
			}
			case 89: // NmTokens = NmTokens NMTOKEN
			{
					((ArrayList) _symbols[offset + 1].value).add(_symbols[offset + 2]); return _symbols[offset + 1];
			}
			case 90: // NmTokens = NMTOKEN
			{
					ArrayList lst = new ArrayList(); lst.add(_symbols[offset + 1]); return new Symbol(lst);
			}
			case 4: // Declaration = MarkupDecl
			case 5: // Declaration = PE_REF
			case 6: // MarkupDecl = ElementDecl
			case 7: // MarkupDecl = AttlistDecl
			case 8: // MarkupDecl = EntityDecl
			case 9: // MarkupDecl = NotationDecl
			case 16: // EntityDecl = GEDecl
			case 17: // EntityDecl = PEDecl
			case 20: // ContentSpec = EMPTY
			case 21: // ContentSpec = ANY
			case 22: // ContentSpec = Mixed
			case 23: // ContentSpec = Children
			case 35: // Children = Choice
			case 39: // Children = Seq
			case 43: // AttType = StringType
			case 44: // AttType = TokenizedType
			case 45: // AttType = EnumeratedType
			case 46: // DefaultDecl = REQUIRED
			case 47: // DefaultDecl = IMPLIED
			case 48: // DefaultDecl = STRING
			case 50: // EntityDef = STRING
			case 51: // EntityDef = ExternalID
			case 53: // PEDef = STRING
			case 54: // PEDef = ExternalID
			case 62: // StringType = CDATA_TYPE
			case 63: // TokenizedType = ID_TYPE
			case 64: // TokenizedType = IDREF_TYPE
			case 65: // TokenizedType = IDREFS_TYPE
			case 66: // TokenizedType = ENTITY_TYPE
			case 67: // TokenizedType = ENTITIES_TYPE
			case 68: // TokenizedType = NMTOKEN_TYPE
			case 69: // TokenizedType = NMTOKENS_TYPE
			case 70: // EnumeratedType = NotationType
			case 71: // EnumeratedType = Enumeration
			case 73: // Cp = NAME
			case 77: // Cp = Choice
			case 81: // Cp = Seq
			{
				return _symbols[offset + 1];
			}
			case 27: // ExternalID = SYSTEM SYSTEM_LITERAL
			case 29: // PublicID = PUBLIC STRING
			case 34: // Names = PIPE NAME
			case 36: // Children = Choice QUESTION
			case 37: // Children = Choice STAR
			case 38: // Children = Choice PLUS
			case 40: // Children = Seq QUESTION
			case 41: // Children = Seq STAR
			case 42: // Children = Seq PLUS
			case 49: // DefaultDecl = FIXED STRING
			case 52: // EntityDef = ExternalID NDataDecl
			case 57: // ChoiceCps = PIPE Cp
			case 61: // SeqCps = COMMA Cp
			case 72: // NDataDecl = NDATA NAME
			case 74: // Cp = NAME QUESTION
			case 75: // Cp = NAME STAR
			case 76: // Cp = NAME PLUS
			case 78: // Cp = Choice QUESTION
			case 79: // Cp = Choice STAR
			case 80: // Cp = Choice PLUS
			case 82: // Cp = Seq QUESTION
			case 83: // Cp = Seq STAR
			case 84: // Cp = Seq PLUS
			{
				return _symbols[offset + 2];
			}
			case 24: // AttDef = NAME AttType DefaultDecl
			case 28: // ExternalID = PUBLIC STRING SYSTEM_LITERAL
			case 30: // Mixed = LPAREN PCDATA RPAREN_STAR
			case 32: // Mixed = LPAREN PCDATA RPAREN
			case 33: // Names = Names PIPE NAME
			case 56: // ChoiceCps = ChoiceCps PIPE Cp
			case 58: // Seq = LPAREN Cp RPAREN
			case 60: // SeqCps = SeqCps COMMA Cp
			case 87: // Enumeration = LPAREN NMTOKEN RPAREN
			{
				return _symbols[offset + 3];
			}
			case 31: // Mixed = LPAREN PCDATA Names RPAREN_STAR
			case 55: // Choice = LPAREN Cp ChoiceCps RPAREN
			case 59: // Seq = LPAREN Cp SeqCps RPAREN
			case 85: // NotationType = NOTATION_TYPE LPAREN NAME RPAREN
			case 88: // Enumeration = LPAREN NMTOKEN NmTokens RPAREN
			{
				return _symbols[offset + 4];
			}
			case 86: // NotationType = NOTATION_TYPE LPAREN NAME Names RPAREN
			{
				return _symbols[offset + 5];
			}
			default:
				throw new IllegalArgumentException("unknown production #" + rule_num);
		}
	}
}
