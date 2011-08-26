/**
 * RegexTag.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.model;

import higraph.model.taggedInterfaces.Tag;

import java.util.ArrayList;
import java.util.List;

import regex.model.payload.AlternationPayload;
import regex.model.payload.KleenePlusPayload;
import regex.model.payload.KleeneStarPayload;
import regex.model.payload.OptionalPayload;
import regex.model.payload.SequencePayload;
import regex.model.payload.TerminalPayload;

/**
 * @author Xiaoyu Guo
 */
public enum RegexTag implements Tag<RegexTag, RegexPayload>, IDescribable{
    SEQUENCE{
        @Override
        public boolean contentModel(List<RegexTag> seq) {
            // Sequence may have 0 or more children, but none can be sequence.
            boolean validated = true;
            if(seq == null || seq.size() == 0){
                validated = true;
            }
            if(seq.contains(SEQUENCE) == true){
                validated = false;
            }
            return validated;
        }

        @Override
        public RegexPayload defaultPayload() {
            return new SequencePayload();
        }

        @Override
        public List<RegexTag> defaultTagSequence() {
            return new ArrayList<RegexTag>(0);
        }
        
        @Override
        public String getDescription(){
            return "SEQ";
        };

    },
    ALTERNATION{
        @Override
        public boolean contentModel(List<RegexTag> seq) {
            // Alternation must have 2 or more children, all must be sequences.
            if(seq == null || seq.size() < 2){
                return false;
            }

            for(int i = 0; i < seq.size(); i++){
                if(seq.get(i) != SEQUENCE){
                    return false;
                }
            }
            return true;
        }

        @Override
        public RegexPayload defaultPayload() {
            return new AlternationPayload();
        }

        @Override
        public List<RegexTag> defaultTagSequence() {
            ArrayList<RegexTag> seq = new ArrayList<RegexTag>(2);
            seq.add(SEQUENCE);
            seq.add(SEQUENCE);
            return seq;
        }
        
        @Override
        public String getDescription(){
            return "ALT";
        };

    },
    KLEENE_PLUS{
        @Override
        public boolean contentModel(List<RegexTag> seq) {
            // Kleene Plus nodes must have exactly 1 sequence as child
            if(seq != null && seq.size() == 1 && seq.get(0) == SEQUENCE){
                return true;
            }
            else{
                return false;
            }
        }

        @Override
        public RegexPayload defaultPayload() {
            return new KleenePlusPayload();
        }

        @Override
        public List<RegexTag> defaultTagSequence() {
            ArrayList<RegexTag> seq = new ArrayList<RegexTag>(1);
            seq.add(SEQUENCE);
            return seq;
        }
        
        @Override
        public String getDescription(){
            return "KLN+";
        };

    },
    KLEENE_STAR{
        @Override
        public boolean contentModel(List<RegexTag> seq) {
            // Kleene Star nodes must have exactly 1 sequence as child
            if(seq != null && seq.size() == 1 && seq.get(0) == SEQUENCE){
                return true;
            }
            else{
                return false;
            }
        }

        @Override
        public RegexPayload defaultPayload() {
            return new KleeneStarPayload();
        }

        @Override
        public List<RegexTag> defaultTagSequence() {
            ArrayList<RegexTag> seq = new ArrayList<RegexTag>(1);
            seq.add(SEQUENCE);
            return seq;
        }
        
        @Override
        public String getDescription(){
            return "KLN*";
        };

    },
    TERMINAL{
        @Override
        public boolean contentModel(List<RegexTag> seq) {
            // Terminal nodes do not have child
            if(seq == null || seq.size() == 0){
                return true;
            }
            else{
                return false;
            }
        }

        @Override
        public RegexPayload defaultPayload() {
            return new TerminalPayload();
        }

        @Override
        public List<RegexTag> defaultTagSequence() {
            return new ArrayList<RegexTag>(0);
        }
        
        @Override
        public String getDescription(){
            return "TER";
        };

    },
    OPTIONAL{
        @Override
        public boolean contentModel(List<RegexTag> seq) {
            // Optional nodes must have exactly 1 sequence as child
            if(seq != null && seq.size() == 1 && seq.get(0) == SEQUENCE){
                return true;
            }
            else{
                return false;
            }
        }

        @Override
        public RegexPayload defaultPayload() {
            return new OptionalPayload();
        }

        @Override
        public List<RegexTag> defaultTagSequence() {
            ArrayList<RegexTag> seq = new ArrayList<RegexTag>(1);
            seq.add(SEQUENCE);
            return seq;
        }
        
        @Override
        public String getDescription(){
            return "OPT";
        };
    } ;

}
