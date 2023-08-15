package org.zeith.mccue.client.sdk.razer.chroma.jna.effects;

import com.sun.jna.Structure;
import org.zeith.mccue.client.sdk.razer.chroma.jna.utils.ColorRef;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the reactive effect: Each key will be lit up for a small duration after being pressed
 */
public class ReactiveKeyboardEffect extends KeyboardEffect {
    private final EffectDuration duration;
    private final ColorRef color;

    public ReactiveKeyboardEffect(EffectDuration duration, ColorRef color) {
        super();
        this.duration = duration;
        this.color = color;
    }

    @Override
    public KeyboardEffectType getType() {
        return KeyboardEffectType.REACTIVE;
    }

    @Override
    public Structure createParameter() {
        ReactiveStructure struct = new ReactiveStructure();
        struct.duration = duration.ordinal();
        struct.color = color.getValue();
        return struct;
    }

    public static class ReactiveStructure extends Structure implements Structure.ByReference {

        public int duration;
        public int color;

        @Override
        protected List getFieldOrder() {
            return Arrays.asList("duration", "color");
        }
    }
}
