package me.olshevski.timelapse.pref;

import ru.noties.spg.anno.SPGKey;
import ru.noties.spg.anno.SPGPreference;

@SPGPreference
class General {

    boolean soundsEnabled;

    @SPGKey(defaultValue = "5")
    int time;

}
