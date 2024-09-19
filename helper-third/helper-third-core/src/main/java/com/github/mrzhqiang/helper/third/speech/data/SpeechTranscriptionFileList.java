package com.github.mrzhqiang.helper.third.speech.data;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 批量转录结果的文件列表数据。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/batch-transcription-get?pivots=rest-api#get-transcription-results">get-transcription-results</a>
 */
@Data
public class SpeechTranscriptionFileList {

    private List<SpeechTranscriptionFile> values = Lists.newArrayList();

}
