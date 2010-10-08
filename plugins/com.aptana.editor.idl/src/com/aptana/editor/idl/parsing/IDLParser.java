package com.aptana.editor.idl.parsing;

import java.util.ArrayList;
import com.aptana.parsing.ast.IParseRootNode;
import beaver.*;
import com.aptana.parsing.IParser;
import com.aptana.parsing.ast.IParseNode;
import com.aptana.parsing.IParseState;

/**
 * This class is a LALR parser generated by
 * <a href="http://beaver.sourceforge.net">Beaver</a> v0.9.6.1
 * from the grammar specification "IDL.grammar".
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class IDLParser extends Parser implements IParser {

	static final ParsingTables PARSING_TABLES = new ParsingTables(
		"U9obbmcOLCKHn$zlPdV1gD45JG2D0bxnYXmhYC8Y2igoAqSK4S9z2QmQxqJKG4pabCC31Lq" +
		"k5H0H106LSrMC5oWeYRSa4WyyK1DpQQBbhz$hcNqpExDyVddx$LzrL$#hkhgwNq#j57obCf" +
		"shQzHP5JfRvJf4xNIMJb5BjTQfQgiJTP8QgbIFgvEIo8lKoUxL3rMe8jLJVUqNsj#kMTWNg" +
		"zmUos7l60uXGmVbO1Hh5fQ7wmVwXHPhJSW#G8TvYulp#FovVk8P##TXz5Ll7zSZUHX7wY5L" +
		"s3nyDDrdv4jpCABCR4n7lZcMcLATmDYDkaF7wsITemIPcwVvMg25kbS7iwwUPEumNQl9wWF" +
		"x9jXN4qTpDTCLEfzvxrOFNPMQmsxMPVgbBi#OupPTcTwTyNQh3jJGZDs9OYZMAXz3nAtIVH" +
		"gaQssT2cBScjXwQeJkX7iptFlrC97qr33YwgKZT85cQwIMwoZT5Mekz7EiJA#p7p5qru$qO" +
		"pLIOpKn2tDuD55CZkQttPgZOzL0mxKfDdzyzwV1ZlmsrJ7URqryZf5YHwoVwepq2bt$E8sA" +
		"zss6VftHVrThVT$vEfJr$QmculI#k$NTGhRkYStcTgbKwtcdT9MSePcw5DxSqAfOBJXlhjL" +
		"A9MgZqtIwEgYzEkfChT0YTL5NTMEq8#$#6g3pD523DSGUrsYDqQzqYQsmvVQGBTDr6gk9kb" +
		"utQ9n#O$FrM$rElzS4C4wJD4LJcMkwPj2xrMRQRNQVhTSIFQ07jSnMwn6jrgDwJD86rJph5" +
		"2Z1QIdahnv$ZSBCxy$tTY0dvo3EPJ7hBk64j#6CHx89qNTWrMu5dKBxezZFGavFFNQljhrZ" +
		"EEHd561VnAuVGkvTtXsxCU$8ebaUdqsvYo96a8VHFCt85rT3LbN8#dElF3FoYXoCL6PIaSS" +
		"p4usrPCVRiUTdiBzD#BuFvvuwWficoRhQahc0$IyZZeZTdJZEukuz9Pot5IUvJtgECTmm9t" +
		"CoCxD$SNgyDndiXgmTGq4wXgZVXPDUphoDCxWriRNbEqxDUoBj19f1OLoTcMCuzy5WxlvHN" +
		"XFnHgVdE0$syNn3lRYJp#U6KBYMlaHHvha1w#od8SGGdp###mrXHzn6QRyryJb6YXsnJiXQ" +
		"OP3LJ#OTTxvSn0rY4QVsFLgVorRcRD4xfHk6TMjkGSTpLWNuAWfR3VpVCU3Oq6S5krI8BA6" +
		"Nu1rmEnQo3qL4w6oQSaQIwR#0EwK5jqAIh9$Bko6$08r2kFu0tWNS5gdH#jYwhwR6c$EGvC" +
		"O5gOUxKAeU7ttzCzYQbc2Sw#S2pn3G6avNP2klM#Tb1Gu$z#tgEdnq0EM#VOcNdtcv1Dy9P" +
		"1co7HWJwOEnjVq4LtevsyigB#TwES$B#LvEuzKFEHqizBhLO6tA9$5$w$LhOlFuzGNhmGRV" +
		"tfXdRLiPEnZPt$TNveXxCw$taKkHpP2Rm9EUNmLAwLV7V5RI$mCWDzpU2fxnVfu5pu7dmGj" +
		"kDtFCjOBN3SWNmGBQuv3R$PYBzHNG5spnkYUyd0gUyksTFevTj5y5Ru7JmELWb#Uy2Tu5xu" +
		"1jEV9of9SVU$xfkV8NYtinU0by27QWk1Jv7jZZ$RIWlGXy4BFv57pYnzdhu3Emru#z39O1V" +
		"pw3oLdnLP8#rgSjTSNbkNF1yYpTsx4uJjs7$Swi$aGldmOkjusHk$SLn$S5Va$wFsmdxej3" +
		"$dR3cnBFHHvV0$rwLsNbOItuCejR4ckNvlUfw$BCrTxBgy4rCVqqRpSpfflaTM#uTNZTB2$" +
		"7UrdXvKqnkwgwrwkzMRQ3w#O7mxBioxBwL6n1PHtsdNFeHiRQoPYlQld7lhKdxGchjissAQ" +
		"tx1cmCMr#3$zY$x9$s3$lI$cv$ipts1RhFaH$R1$QUj$WB#BFjidViRNlBthGtx7LxpLwrF" +
		"zaVRQUzOY$R3i$SRY$PCyYdZRlDNhHjjjLUeBK5F6NFstFshFtLthSFxICqdp8FtxFjjK$i" +
		"A$ktOezji3cXVDJMnhGFnzeFsX9RR8jyRq7ulZyzEjqcs2Ja19l8Us6es#F7Ra#pfjXaMse" +
		"Fq9fhSwmAt6spxIwxqsRP7NQFCXvRu#KqkoMKKtr$bPShmV$ilpJ7Kia5L6f9aF08g26I8C" +
		"7lRH84r2IrqHFqOhoN2gXK2a4HzKgIYYL9dU8GKBC81DHd0W5rbq3WVWT1G4LJHEqJPA1TY" +
		"CJta0MWaFefuP44GLXDHKXG8GKXIXXprKuRq9v#DZeGJodLzL36Xn9ZEVnov23673eoNe6k" +
		"0jsPpDi9N8Xk66CE8pr6O3yYbAucha4VV7SDgsnHJIK0lzN8$iXHoE4X0lSVEH1LN50BtV4" +
		"n03c0yO7m1Y9TnJKOdSDescL8XzwCzw3V0qxbTosTpiS=");

	/*
	 * (non-Javadoc)
	 * @see com.aptana.parsing.IParser#parse(com.aptana.parsing.IParseState)
	 */
	public synchronized IParseRootNode parse(IParseState parseState) throws java.lang.Exception
	{
		IDLScanner scanner = new IDLScanner();
		
		// grab source
		char[] characters = parseState.getSource();

		// make sure we have some source
		String source = (characters != null) ? new String(characters) : "";

		// send source to the scanner
		scanner.setSource(source);

		// parse
		IParseRootNode result = (IParseRootNode) parse(scanner);
		
		// save reference to result
		parseState.setParseResult(result);
		
		return result;
	}

	public IDLParser() {
		super(PARSING_TABLES);
	}

	protected Symbol invokeReduceAction(int rule_num, int offset) {
		switch(rule_num) {
			case 7: // ExtendedAttributes = ExtendedAttributes COMMA ExtendedAttribute
			{
					((ArrayList) _symbols[offset + 1].value).add(_symbols[offset + 3]); return _symbols[offset + 1];
			}
			case 8: // ExtendedAttributes = ExtendedAttribute
			{
					ArrayList lst = new ArrayList(); lst.add(_symbols[offset + 1]); return new Symbol(lst);
			}
			case 21: // ArgumentList = ArgumentList COMMA Argument
			{
					((ArrayList) _symbols[offset + 1].value).add(_symbols[offset + 3]); return _symbols[offset + 1];
			}
			case 22: // ArgumentList = Argument
			{
					ArrayList lst = new ArrayList(); lst.add(_symbols[offset + 1]); return new Symbol(lst);
			}
			case 23: // opt$ExtendedAttributeList = 
			case 25: // opt$IN = 
			case 27: // opt$OPTIONAL = 
			case 29: // opt$ELLIPSIS = 
			case 70: // InterfaceInheritance = 
			case 72: // InterfaceMembers = 
			case 77: // ExceptionMembers = 
			case 93: // ReadOnly = 
			case 95: // GetRaises = 
			case 97: // SetRaises = 
			case 102: // Specials = 
			case 110: // OptionalIdentifier = 
			case 112: // Raises = 
			case 121: // ScopedNames = 
			{
				return new Symbol(null);
			}
			case 0: // $goal = Definitions
			case 4: // Definitions = Definition
			case 9: // ExtendedAttribute = ExtendedAttributeNoArg
			case 10: // ExtendedAttribute = ExtendedAttributeArgList
			case 11: // ExtendedAttribute = ExtendedAttributeNamedArgList
			case 12: // ExtendedAttribute = ExtendedAttributeIdent
			case 13: // ExtendedAttribute = ExtendedAttributeScopedName
			case 14: // ExtendedAttributeNoArg = IDENTIFIER
			case 24: // opt$ExtendedAttributeList = ExtendedAttributeList
			case 26: // opt$IN = IN
			case 28: // opt$OPTIONAL = OPTIONAL
			case 30: // opt$ELLIPSIS = ELLIPSIS
			case 32: // Type = NullableType
			case 33: // Type = ScopedName
			case 34: // Type = ANY
			case 35: // Type = OBJECT
			case 36: // NullableType = UnsignedIntegerType
			case 38: // NullableType = BOOLEAN
			case 40: // NullableType = OCTET
			case 42: // NullableType = FLOAT
			case 44: // NullableType = DOUBLE
			case 46: // NullableType = DOMSTRING
			case 53: // UnsignedIntegerType = IntegerType
			case 54: // IntegerType = SHORT
			case 55: // IntegerType = LONG
			case 57: // ScopedName = AbsoluteScopedName
			case 58: // ScopedName = RelativeScopedName
			case 62: // Definition = Module
			case 63: // Definition = Interface
			case 64: // Definition = Exception
			case 65: // Definition = Typedef
			case 66: // Definition = ImplementsStatement
			case 73: // InterfaceMember = Const
			case 74: // InterfaceMember = AttributeOrOperation
			case 81: // ConstExpr = BooleanLiteral
			case 82: // ConstExpr = NUMBER
			case 83: // BooleanLiteral = TRUE
			case 84: // BooleanLiteral = FALSE
			case 86: // AttributeOrOperation = Attribute
			case 87: // AttributeOrOperation = Operation
			case 88: // StringifierAttributeOrOperation = Attribute
			case 89: // StringifierAttributeOrOperation = OperationRest
			case 90: // StringifierAttributeOrOperation = SEMICOLON
			case 92: // ReadOnly = READONLY
			case 100: // OmittableSpecials = Specials
			case 103: // Special = GETTER
			case 104: // Special = SETTER
			case 105: // Special = CREATOR
			case 106: // Special = DELETER
			case 107: // Special = CALLER
			case 109: // OptionalIdentifier = IDENTIFIER
			case 114: // ExceptionMember = Const
			case 115: // ExceptionMember = ExceptionField
			case 117: // ReturnType = Type
			case 118: // ReturnType = VOID
			{
				return _symbols[offset + 1];
			}
			case 2: // Definitions = Definitions Definition
			case 3: // Definitions = ExtendedAttributeList Definition
			case 6: // ExtendedAttributeList = LBRACKET RBRACKET
			case 37: // NullableType = UnsignedIntegerType QUESTION
			case 39: // NullableType = BOOLEAN QUESTION
			case 41: // NullableType = OCTET QUESTION
			case 43: // NullableType = FLOAT QUESTION
			case 45: // NullableType = DOUBLE QUESTION
			case 47: // NullableType = DOMSTRING QUESTION
			case 50: // UnsignedIntegerType = UNSIGNED SHORT
			case 51: // UnsignedIntegerType = UNSIGNED LONG
			case 56: // IntegerType = LONG LONG
			case 60: // AbsoluteScopedName = DOUBLE_COLON IDENTIFIER
			case 61: // RelativeScopedName = IDENTIFIER AbsoluteScopedName
			case 69: // InterfaceInheritance = COLON ScopedNameList
			case 85: // AttributeOrOperation = STRINGIFIER StringifierAttributeOrOperation
			case 94: // GetRaises = GETRAISES ExceptionList
			case 96: // SetRaises = SETRAISES ExceptionList
			case 98: // Operation = OmittableSpecials OperationRest
			case 99: // OmittableSpecials = OMITTABLE Specials
			case 101: // Specials = Special Specials
			case 111: // Raises = RAISES ExceptionList
			case 119: // ScopedNameList = ScopedName ScopedNames
			{
				return _symbols[offset + 2];
			}
			case 1: // Definitions = Definitions ExtendedAttributeList Definition
			case 5: // ExtendedAttributeList = LBRACKET ExtendedAttributes RBRACKET
			case 15: // ExtendedAttributeArgList = IDENTIFIER LPAREN RPAREN
			case 19: // ExtendedAttributeIdent = IDENTIFIER EQUAL IDENTIFIER
			case 20: // ExtendedAttributeScopedName = IDENTIFIER EQUAL ScopedName
			case 52: // UnsignedIntegerType = UNSIGNED LONG LONG
			case 59: // AbsoluteScopedName = AbsoluteScopedName DOUBLE_COLON IDENTIFIER
			case 71: // InterfaceMembers = ExtendedAttributeList InterfaceMember InterfaceMembers
			case 76: // ExceptionMembers = ExtendedAttributeList ExceptionMember ExceptionMembers
			case 113: // ExceptionList = LPAREN ScopedNameList RPAREN
			case 116: // ExceptionField = Type IDENTIFIER SEMICOLON
			case 120: // ScopedNames = COMMA ScopedName ScopedNames
			{
				return _symbols[offset + 3];
			}
			case 16: // ExtendedAttributeArgList = IDENTIFIER LPAREN ArgumentList RPAREN
			case 48: // NullableType = SEQUENCE LESS_THAN Type GREATER_THAN
			case 78: // Typedef = TYPEDEF Type IDENTIFIER SEMICOLON
			case 79: // ImplementsStatement = ScopedName IMPLEMENTS ScopedName SEMICOLON
			{
				return _symbols[offset + 4];
			}
			case 17: // ExtendedAttributeNamedArgList = IDENTIFIER EQUAL IDENTIFIER LPAREN RPAREN
			case 49: // NullableType = SEQUENCE LESS_THAN Type GREATER_THAN QUESTION
			{
				return _symbols[offset + 5];
			}
			case 18: // ExtendedAttributeNamedArgList = IDENTIFIER EQUAL IDENTIFIER LPAREN ArgumentList RPAREN
			case 31: // Argument = opt$ExtendedAttributeList opt$IN opt$OPTIONAL Type opt$ELLIPSIS IDENTIFIER
			case 67: // Module = MODULE IDENTIFIER LCURLY Definitions RCURLY SEMICOLON
			case 75: // Exception = EXCEPTION IDENTIFIER LCURLY ExceptionMembers RCURLY SEMICOLON
			case 80: // Const = CONST Type IDENTIFIER EQUAL ConstExpr SEMICOLON
			{
				return _symbols[offset + 6];
			}
			case 68: // Interface = INTERFACE IDENTIFIER InterfaceInheritance LCURLY InterfaceMembers RCURLY SEMICOLON
			case 91: // Attribute = ReadOnly ATTRIBUTE Type IDENTIFIER GetRaises SetRaises SEMICOLON
			case 108: // OperationRest = ReturnType OptionalIdentifier LPAREN ArgumentList RPAREN Raises SEMICOLON
			{
				return _symbols[offset + 7];
			}
			default:
				throw new IllegalArgumentException("unknown production #" + rule_num);
		}
	}
}
