package me.avo.kumo.nhk.processing

import org.apache.commons.compress.compressors.gzip.*
import org.apache.commons.lang3.*
import org.deeplearning4j.models.embeddings.inmemory.*
import org.deeplearning4j.models.embeddings.learning.impl.elements.*
import org.deeplearning4j.models.embeddings.loader.*
import org.deeplearning4j.models.word2vec.*
import org.deeplearning4j.models.word2vec.wordstore.*
import org.deeplearning4j.models.word2vec.wordstore.inmemory.*
import org.nd4j.linalg.api.ndarray.*
import org.nd4j.linalg.factory.*
import org.nd4j.linalg.ops.transforms.*
import java.io.*
import java.util.zip.*

object Word2VecFix {

    fun loadGoogleBinaryModel(modelFile: File, lineBreaks: Boolean): Word2Vec = readBinaryModel(modelFile, lineBreaks, true)

    private fun readBinaryModel(modelFile: File, linebreaks: Boolean, normalize: Boolean): Word2Vec {
        var lookupTable: InMemoryLookupTable<VocabWord>? = null
        var cache: VocabCache<VocabWord> = AbstractCache()
        var syn0: INDArray? = null
        var words: Int
        var size: Int

        val originalFreq = Nd4j.getMemoryManager().occasionalGcFrequency
        val originalPeriodic = Nd4j.getMemoryManager().isPeriodicGcActive

        if (originalPeriodic)
            Nd4j.getMemoryManager().togglePeriodicGc(false)

        Nd4j.getMemoryManager().occasionalGcFrequency = 50000

        try {
            BufferedInputStream(if (GzipUtils.isCompressedFilename(modelFile.name))
                GZIPInputStream(FileInputStream(modelFile))
            else
                FileInputStream(modelFile)).use { bis ->
                DataInputStream(bis).use { dis ->
                    words = Integer.parseInt(WordVectorSerializer.readString(dis))
                    size = Integer.parseInt(WordVectorSerializer.readString(dis))
                    syn0 = Nd4j.create(words, size)
                    cache = AbstractCache()

                    WordVectorSerializer.printOutProjectedMemoryUse(words.toLong(), size, 1)

                    lookupTable = InMemoryLookupTable.Builder<VocabWord>().cache(cache)
                            .useHierarchicSoftmax(false).vectorLength(size).build() as InMemoryLookupTable<VocabWord>

                    var word: String
                    val vector = FloatArray(size)
                    for (i in 0 until words) {

                        word = WordVectorSerializer.readString(dis)
                        //println("Loading $word with word $i")

                        for (j in 0 until size) {
                            vector[j] = WordVectorSerializer.readFloat(dis)
                        }

                        syn0!!.putRow(i, if (normalize) Transforms.unitVec(Nd4j.create(vector)) else Nd4j.create(vector))

                        // FIXME There was an empty string in my test model ......
                        if (StringUtils.isNotEmpty(word)) {
                            val vw = VocabWord(1.0, word)
                            vw.index = cache.numWords()

                            cache.addToken(vw)
                            cache.addWordToIndex(vw.index, vw.label)

                            cache.putVocabWord(word)
                        }

                        if (linebreaks) {
                            dis.readByte() // line break
                        }

                        Nd4j.getMemoryManager().invokeGcOccasionally()
                    }
                }
            }
        } finally {
            if (originalPeriodic)
                Nd4j.getMemoryManager().togglePeriodicGc(true)

            Nd4j.getMemoryManager().occasionalGcFrequency = originalFreq
        }

        lookupTable!!.syn0 = syn0!!

        val ret = Word2Vec.Builder().useHierarchicSoftmax(false).resetModel(false).layerSize(syn0!!.columns())
                .allowParallelTokenization(true).elementsLearningAlgorithm(SkipGram())
                .learningRate(0.025).windowSize(5).workers(1).build()

        ret.setVocab(cache)
        ret.setLookupTable(lookupTable!!)

        return ret
    }

}