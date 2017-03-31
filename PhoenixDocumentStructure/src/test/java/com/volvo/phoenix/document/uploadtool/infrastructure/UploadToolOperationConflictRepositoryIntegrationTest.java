package com.volvo.phoenix.document.uploadtool.infrastructure;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.translator.UploadToolOperationConflictTranslator;
import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolOperationConflictDTO;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationConflictRepository;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationConflict;
import com.volvo.phoenix.orion.util.SqlUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.fest.assertions.Assertions.assertThat;

public class UploadToolOperationConflictRepositoryIntegrationTest extends AbstractTransactionalTestCase {

    private static final String CREATE_TEST_DATA = "./src/test/resources/createUploadToolOperationConflictTestData.sql";

    @Autowired
    private SqlUtils sqlUtils;

    @Autowired
    @Named("dataSource")
    private DataSource dataSource;

    @Autowired
    private UploadToolOperationConflictRepository utocr;

    @Test
    public void shouldHaveOneConflict() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[]{});
        // when
        List<UploadToolOperationConflict> list = utocr.findByOperationId(-1l);
        // then
        assertThat(list).isNotNull();
        assertThat(list.size()).isGreaterThan(0);
    }

    @Test
    public void shouldBeParsedProperly() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[]{});
        // when
        UploadToolOperationConflictDTO dto = UploadToolOperationConflictTranslator.toDTO(utocr.findOne(-1l));
        // then
        assertThat(dto.getConflict()).isEqualTo("Folder conflict");
        assertThat(dto.getFolderPath()).isEqualTo("Marine");
    }
}
