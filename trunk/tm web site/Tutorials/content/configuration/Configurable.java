package tm.interfaces;

import tm.configuration.Configuration;
/*
COPYRIGHT (C) 1999 by Michael Bruce-Lockhart.  The associated software is
released to students for educational purposes only and only for the duration
of the course in which it is handed out. No other use of the software, either
commercial or non-commercial, may be made without the express permission of
the author.
*/

public interface Configurable{
    public void notifyOfSave(Configuration config);
    public void notifyOfLoad(Configuration config);
    public void dispose();
}
    