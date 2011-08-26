/*
 * Created on 2010-01-18 by Theodore S. Norvell. 
 */
package demo.model;

import java.util.List;

import higraph.model.taggedInterfaces.Tag;

public enum DemoTag implements Tag<DemoTag, DemoPayload> {
    ASSIGN {
        @Override
        public boolean contentModel(List<DemoTag> seq) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DemoPayload defaultPayload() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<DemoTag> defaultTagSequence() {
            // TODO Auto-generated method stub
            return null;
        }
        
    },
    SEQ{
        @Override
        public boolean contentModel(List<DemoTag> seq) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DemoPayload defaultPayload() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<DemoTag> defaultTagSequence() {
            // TODO Auto-generated method stub
            return null;
        }

    } ;

}
