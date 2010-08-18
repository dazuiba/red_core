package com.aptana.ruby.formatter.internal.nodes;

import com.aptana.formatter.FormatterTextNode;
import com.aptana.formatter.IFormatterContext;
import com.aptana.formatter.IFormatterDocument;
import com.aptana.formatter.IFormatterWriter;
import com.aptana.ruby.formatter.RubyFormatterConstants;

public class FormatterCommentNode extends FormatterTextNode {

	public FormatterCommentNode(IFormatterDocument document, int startOffset,
			int endOffset) {
		super(document, startOffset, endOffset);
	}

	public void accept(IFormatterContext context, IFormatterWriter visitor)
			throws Exception {
		if (getDocument().getBoolean(RubyFormatterConstants.WRAP_COMMENTS)) {
			final boolean savedWrapping = context.isWrapping();
			context.setWrapping(true);
			visitor.write(context, getStartOffset(), getEndOffset());
			context.setWrapping(savedWrapping);
		} else {
			visitor.write(context, getStartOffset(), getEndOffset());
		}
	}

}