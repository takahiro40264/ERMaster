package ermaster.editor.model.diagram_contents.not_element.sequence;

import ermaster.ResourceString;
import ermaster.editor.model.diagram_contents.not_element.ObjectSet;

public class SequenceSet extends ObjectSet<Sequence> {

	private static final long serialVersionUID = -120487815554383179L;

	@Override
	public SequenceSet clone() {
		return (SequenceSet) super.clone();
	}

	public String getName() {
		return ResourceString.getResourceString("label.object.type.sequence_list");
	}

}
