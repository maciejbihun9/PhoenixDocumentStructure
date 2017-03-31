package com.volvo.phoenix.document.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.entity.Folder;

public class FolderRepositoryIntegrationTest extends AbstractTransactionalTestCase {

    @Autowired
    private FolderRepository folderRepository;

    @PersistenceContext(unitName = "PhoenixDocumentStructurePU")
    private EntityManager em;

    @Test
    public void shouldCreateAndFindFolder() {
        // given
        Folder folder = prepareFolder();

        // when
        Folder foundFolder = folderRepository.findOne(folder.getId());

        // then
        Assertions.assertThat(foundFolder).isNotNull();
    }

    @Test
    public void shouldFindRootFolders() {
        // given
        Folder rootFolder = new Folder();
        rootFolder.setId(-1L);
        rootFolder.setOwnerRealname("owner");
        rootFolder.setText("text");
        rootFolder.setType(NodeType.M);
        folderRepository.save(rootFolder);

        // when
        List<Folder> foundFolders = folderRepository.findByParentIsNullOrderByTextAsc();

        // then
        Assertions.assertThat(foundFolders).isNotNull();
        Assertions.assertThat(foundFolders.size()).isGreaterThan(0);
    }

    private Folder prepareFolder() {
        Folder folder = new Folder();

        folder.setText("some folder");
        folder.setType(NodeType.M);

        folderRepository.save(folder);

        em.flush();
        em.clear();
        return folder;
    }
}
