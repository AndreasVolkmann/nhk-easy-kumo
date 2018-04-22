package me.avo.kumo.nhk.processing.audio;

import org.jcodec.common.Codec;
import org.jcodec.common.Tuple;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CodecFinder {

    @NotNull
    @Contract(pure = true)
    public static Tuple._3<Integer, Integer, Codec> getCodec(Integer f, Integer index) {
        return new Tuple._3<>(f, index, Codec.AAC);
    }

}
