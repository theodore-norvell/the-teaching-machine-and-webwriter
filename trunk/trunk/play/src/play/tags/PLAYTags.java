package play.tags;

import higraph.model.taggedInterfaces.Tag;

import java.util.ArrayList;
import java.util.List;

import play.model.NodePayloadPLAY;

/**
 * @author Charles
 */
public enum PLAYTags implements Tag<PLAYTags, NodePayloadPLAY> {
	/**
	 * @uml.property name="rOOT"
	 * @uml.associationEnd
	 */
	ROOT {
		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			if (seq.size() != 0) {
				for (int i = 0; i < seq.size(); i++) {
					return seq.get(i) == CLASS;
				}
				return false;
			} else
				return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("root", ROOT);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(CLASS);
			return temp;
		}
	},
	/**
	 * @uml.property name="cLASS"
	 * @uml.associationEnd
	 */
	CLASS {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			if (seq.size() != 0) {
				for (int i = 0; i < seq.size(); i++) {
					return seq.get(i) == SEQ || seq.get(i) == VARDECL;
				}
				return false;
			} else
				return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("class", CLASS);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(SEQ);
			return temp;
		}
	},
	/**
	 * @uml.property name="vARDECL"
	 * @uml.associationEnd
	 */
	VARDECL {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			if (seq.size() == 0)
				return false;
			if (seq.size() == 1)
				return seq.get(0) == WORLDVAR || seq.get(0) == FIELDVAR
						|| seq.get(0) == LOCALVAR || seq.get(0) == EMPTY;
			if (seq.size() == 2) {
				return (seq.get(0) == WORLDVAR || seq.get(0) == FIELDVAR
						|| seq.get(0) == LOCALVAR || seq.get(0) == EMPTY)
						&& (seq.get(1) == NUMBER || seq.get(1) == STRING
								|| seq.get(1) == BOOLEAN || seq.get(1) == NULL
								|| seq.get(1) == EMPTY || seq.get(1) == ANY || seq
								.get(1) == E_PLACE_HOLDER);
			}
			if (seq.size() == 3) {
				return (seq.get(0) == WORLDVAR || seq.get(0) == FIELDVAR
						|| seq.get(0) == LOCALVAR || seq.get(0) == EMPTY)
						&& (seq.get(1) == NUMBER || seq.get(1) == STRING
								|| seq.get(1) == BOOLEAN || seq.get(1) == NULL
								|| seq.get(1) == EMPTY || seq.get(1) == ANY)
						&& seq.get(2) == E_PLACE_HOLDER;
			}
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("vardecl", VARDECL);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(E_PLACE_HOLDER);
			temp.add(E_PLACE_HOLDER);
			return temp;
		}
	},
	/**
	 * @uml.property name="dOT"
	 * @uml.associationEnd
	 */
	DOT {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			if (seq.size() == 0)
				return seq.get(0) == EXP;
			else
				return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("dot", DOT);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(EXP);
			return temp;
		}

	},
	/**
	 * @uml.property name="eXP"
	 * @uml.associationEnd
	 */
	EXP {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			boolean temp = false;
			if (seq.size() != 0) {
				for (int i = 0; i < seq.size(); i++) {
					if (seq.get(i) == CALL || seq.get(i) == WORLDCALL
							|| seq.get(i) == ASSIGN || seq.get(i) == METHOD
							|| seq.get(i) == IF || seq.get(i) == WHILE
							|| seq.get(i) == VARDECL || seq.get(i) == LOCALVAR
							|| seq.get(i) == FIELDVAR || seq.get(i) == WORLDVAR
							|| seq.get(i) == EQUAL_TO)
						temp = true;
					else
						temp = false;
				}
			} else
				return false;
			return temp;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("exp", EXP);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(E_PLACE_HOLDER);
			return temp;
		}

	},
	/**
	 * @uml.property name="iF"
	 * @uml.associationEnd
	 */
	IF {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			if (seq.size() != 3)
				return false;
			else {
				return seq.get(0) == EXP && seq.get(1) == SEQ
						&& seq.get(2) == SEQ;
			}
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("if", IF);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(EXP);
			temp.add(SEQ);
			temp.add(SEQ);
			return temp;
		}

	},
	/**
	 * @uml.property name="sEQ"
	 * @uml.associationEnd
	 */
	SEQ {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			if (seq.size() == 0)
				return true;
			if (seq.size() == 1)
				return true;
			if (seq.size() == 2)
				return (seq.get(0) == EXP && seq.get(1) == SEQ)
						|| (seq.get(0) == VARDECL && seq.get(1) == SEQ);
			else
				return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("seq", SEQ);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(E_PLACE_HOLDER);
			return temp;
		}

	},
	/**
	 * @uml.property name="wHILE"
	 * @uml.associationEnd
	 */
	WHILE {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			if (seq.size() != 2)
				return false;
			else {
				if (seq.get(0) == EXP && seq.get(1) == SEQ)
					return true;
				else
					return false;
			}
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("while", WHILE);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(EXP);
			temp.add(SEQ);
			return temp;
		}

	},
	/**
	 * @uml.property name="bOOLEAN"
	 * @uml.associationEnd
	 */
	BOOLEAN {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("boolean", BOOLEAN);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="sTRING"
	 * @uml.associationEnd
	 */
	STRING {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("string", STRING);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="nUMBER"
	 * @uml.associationEnd
	 */
	NUMBER {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("number", NUMBER);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="eMPTY"
	 * @uml.associationEnd
	 */
	EMPTY {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("empty", EMPTY);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="nULL"
	 * @uml.associationEnd
	 */
	NULL {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("null", NULL);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="tHIS"
	 * @uml.associationEnd
	 */
	THIS {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("this", THIS);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="cALL"
	 * @uml.associationEnd
	 */
	CALL {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			if (seq.size() < 1)
				return false;
			else {
				for (int i = 0; i < seq.size(); i++) {
					if (seq.get(i) != EXP)
						break;
					else
						return true;
				}
				return false;
			}
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("call", CALL);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(EXP);
			return temp;
		}

	},
	/**
	 * @uml.property name="lOCALVAR"
	 * @uml.associationEnd
	 */
	LOCALVAR {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("localVar", LOCALVAR);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="fIELDVAR"
	 * @uml.associationEnd
	 */
	FIELDVAR {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("fieldVar", FIELDVAR);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="wORLDVAR"
	 * @uml.associationEnd
	 */
	WORLDVAR {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("worldVar", WORLDVAR);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="wORLDCALL"
	 * @uml.associationEnd
	 */
	WORLDCALL {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			for (int i = 0; i < seq.size(); i++) {
				if (seq.get(i) != EXP)
					break;
				else
					return true;
			}
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("worldcall", WORLDCALL);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(EXP);
			return temp;
		}

	},
	/**
	 * @uml.property name="aSSIGN"
	 * @uml.associationEnd
	 */
	ASSIGN {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return seq.get(0) == LOCALVAR || seq.get(0) == FIELDVAR || seq
					.get(0) == WORLDVAR
					&& seq.get(1) == ASS_LABEL
					&& seq.get(2) == LOCALVAR || seq.get(2) == FIELDVAR || seq
							.get(2) == WORLDVAR;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("assign", ASSIGN);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(E_PLACE_HOLDER);
			temp.add(ASS_LABEL);
			temp.add(E_PLACE_HOLDER);
			return temp;
		}

	},
	/**
	 * @uml.property name="aSS_LABEL"
	 * @uml.associationEnd
	 */
	ASS_LABEL {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY(":=", ASS_LABEL);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="mETHOD"
	 * @uml.associationEnd
	 */
	METHOD {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			if (seq.size() <= 2)
				return false;
			else
				return (seq.get(seq.size() - 2) == NUMBER
						|| seq.get(seq.size() - 2) == STRING
						|| seq.get(seq.size() - 2) == BOOLEAN
						|| seq.get(seq.size() - 2) == NULL
						|| seq.get(seq.size() - 2) == EMPTY || seq.get(seq
						.size() - 2) == ANY)
						&& seq.get(seq.size() - 1) == SEQ;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("method", METHOD);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			temp.add(E_PLACE_HOLDER);
			temp.add(TYPE_PLACE_HOLDER);
			temp.add(SEQ);
			return temp;

		}

	},

	/**
	 * @uml.property name="tYPE_PLACE_HOLDER"
	 * @uml.associationEnd
	 */
	TYPE_PLACE_HOLDER {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("type", TYPE_PLACE_HOLDER);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			List<PLAYTags> temp = new ArrayList<PLAYTags>();
			return temp;
		}

	},
	/**
	 * @uml.property name="oPTEXP"
	 * @uml.associationEnd
	 */
	OPTEXP {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("optexp", OPTEXP);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="aNY"
	 * @uml.associationEnd
	 */
	ANY {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("any", ANY);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="cOMM"
	 * @uml.associationEnd
	 */
	COMM {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("comm", COMM);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	/**
	 * @uml.property name="e_PLACE_HOLDER"
	 * @uml.associationEnd
	 */
	E_PLACE_HOLDER {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			return true;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			return new NodePayloadPLAY("e_place_holder", E_PLACE_HOLDER);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			return new ArrayList<PLAYTags>();
		}

	},
	EQUAL_TO {

		@Override
		public boolean contentModel(List<PLAYTags> seq) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public NodePayloadPLAY defaultPayload() {
			// TODO Auto-generated method stub
			return new NodePayloadPLAY("==", EQUAL_TO);
		}

		@Override
		public List<PLAYTags> defaultTagSequence() {
			// TODO Auto-generated method stub
			return new ArrayList<PLAYTags>();
		}

	}

}
