package com.github.mrzhqiang.helper.third.translate;

import com.github.mrzhqiang.helper.CollectionUtils;
import com.github.mrzhqiang.helper.StringUtils;
import static com.github.mrzhqiang.helper.third.translate.AzureTranslateTextApi.API_3_0;
import com.github.mrzhqiang.helper.third.translate.data.TranslateContent;
import com.github.mrzhqiang.helper.third.translate.data.TranslateTextCreateBody;
import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TranslateServiceImpl implements TranslateService {

    private final AzureTranslateAuthApi azureTranslateAuthApi;
    private final AzureTranslateTextApi azureTranslateTextApi;
    private final AzureTranslateDocumentApi azureTranslateDocumentApi;

    private String token;
    private LocalDateTime tokenTime = LocalDateTime.now();

    private Map<String, String> azureLangCodeMap;

    @Override
    public TranslateTextData translate(TextTranslateBody body) {
        List<TranslateTextCreateBody> bodyList = ImmutableList.of(TranslateTextCreateBody.of(body.getText()));

        if (!StringUtils.hasText(token) || LocalDateTime.now().isAfter(tokenTime.plusMinutes(9))) {
            token = "Bearer " + azureTranslateAuthApi.issueToken().blockingGet();
            tokenTime = LocalDateTime.now();
        }

        if (azureLangCodeMap == null) {
            try {
                //azureLangCodeMap = Jsons.mapFrom(ResourceUtils.getFile(LANG_CODE_AZURE_JSON), String.class, String.class);
            } catch (Exception ignored) {
                log.warn("Can not found {} in {}, use fallback lang code.",
                         LANG_CODE_AZURE_JSON, System.getProperty(StandardSystemProperty.USER_DIR.key()));
                azureLangCodeMap = Maps.newHashMap();
                //azureLangCodeMap.put(EN, EN);
                //azureLangCodeMap.put(PT, PT);
                //azureLangCodeMap.put(ZH, AZURE_ZH);
            }
        }

        String lang = Optional.ofNullable(body.getLanguageCode())
                .filter(StringUtils::hasText)
                .map(azureLangCodeMap::get)
                .orElse("en");
        List<TranslateContent> translateContents = azureTranslateTextApi.translate(token, bodyList, API_3_0, lang).blockingGet();
        if (!CollectionUtils.isEmpty(translateContents)) {
            return translateContents.stream()
                    .flatMap(it -> it.getTranslations().stream())
                    .filter(it -> lang.equals(it.getTo()))
                    .findFirst()
                    .map(TranslateServiceImpl::toTextData)
                    .orElse(null);
        }
        return null;
    }

    private static TranslateTextData toTextData(TranslateContent.Translation translation) {
        if (translation == null) {
            return null;
        }
        TranslateTextData translateTextData = new TranslateTextData();
        translateTextData.setLanguageCode(translation.getTo());
        translateTextData.setText(translation.getText());
        return translateTextData;
    }

}
