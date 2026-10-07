package com.ragnest.parser;

import java.io.InputStream;

/**
 * 文档解析接口。
 *
 * <p>面向业务场景的解析抽象：输入文件流，输出解析后的纯文本与元数据。</p>
 *
 * <p>具体实现按文件类型区分（PDF / Word / Markdown / Excel 等）。</p>
 */
public interface DocumentParser {

    /**
     * 判断是否能解析指定文件类型。
     *
     * @param fileType 文件类型（扩展名或 MIME）
     * @return true 表示支持
     */
    boolean supports(String fileType);

    /**
     * 解析文档。
     *
     * @param inputStream 文件输入流
     * @param filename    文件名（用于识别类型与元数据）
     * @return 解析结果
     */
    ParsedDocument parse(InputStream inputStream, String filename);
}
