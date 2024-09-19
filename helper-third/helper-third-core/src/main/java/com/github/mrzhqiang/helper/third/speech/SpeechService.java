package com.github.mrzhqiang.helper.third.speech;

public interface SpeechService {

    /**
     * 语音转文本。
     * <p>
     * 这个方法调用 OpenAi 的语音转文本接口。
     *
     * @param dto 语音 URL。
     * @return 包含文本的数据传输对象。
     */
    SpeechTextDto speechToText(SpeechUrlDto dto);

    /**
     * 语音文件转文本。
     *
     * @param files 语音文件字节数组。
     * @return 包含文本的数据传输对象。
     */
    SpeechTextDto speechToText(byte[] files);

    /**
     * 语音转录文本。
     * <p>
     * 这个方法调用 Azure 的批量转录接口，正常情况下支持语音文件的批量转录，对于语音文件数量和语音时长没有明确的限制。
     *
     * @param dto 语音 URL。
     * @return 包含文本的数据传输对象。
     */
    SpeechTextDto transcription(SpeechUrlDto dto);

    /**
     * 短音频转文本。
     * <p>
     * 音频限制为最长 60s 长度。
     *
     * @param dto 语音 URL。
     * @return 包含文本的数据传输对象。
     */
    SpeechTextDto stt(SpeechUrlDto dto);

    /**
     * 文本转语音。
     *
     * @param dto 文本内容。
     * @return 包含语音 URL 的数据传输对象。
     */
    SpeechUrlDto tts(SpeechTextDto dto);

}
