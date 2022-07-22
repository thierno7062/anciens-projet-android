package com.gtemate.petiteannoncekmer.service;

import com.gtemate.petiteannoncekmer.domain.Image;
import com.gtemate.petiteannoncekmer.repository.ImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Image.
 */
@Service
@Transactional
public class ImageService extends BaseEntityService<Image> {

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    // the dimension of the thumbanil
    public final static int THUMBNAIL_DIMENSION = 150;

    @Inject
    private ImageRepository imageRepository;

    @Override
    public ImageRepository getRepository() {
        return imageRepository;
    }

    @Override
    public JpaSpecificationExecutor<Image> getPagineableRepository() {
        return  getRepository();
    }

    /**
     *  Get all the images.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Image> getByDeclaration(Long id) {
        log.debug("Request to get Declaration");
        List<Image> result = imageRepository.findByDeclaration(id);
        return result;
    }

    /**
     *  convert image to thumbnail
     *  @return the converted image
     */
    @Transactional(readOnly = true)
    public Image getThumbNail(MultipartFile multipartFile) {
        Optional<Image> image = createImageFromMultipartFile(multipartFile);
        File tempFile = null;
        Image res = null;
        try {
            tempFile = File.createTempFile("img_", "."+multipartFile.getContentType().split("/")[1]);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(image.get().getContent());

            Thumbnails.of(tempFile).size(THUMBNAIL_DIMENSION,THUMBNAIL_DIMENSION).toFile(tempFile);
            res = new Image();
            byte[] array = Files.readAllBytes(tempFile.toPath());
            image.get().setContent(array);

        } catch (Exception e) {
            log.error("Error when creating thumbnail: {}: {}",res , e);
        }
        return image.get();
    }


    public Optional<Image> createImageFromMultipartFile(MultipartFile multipartFile){

        if (multipartFile == null) {
            return Optional.empty();
        }

        // create Image
        Image image = new Image();

        // set fields
        image.setFileName(multipartFile.getOriginalFilename());
        image.setContentType(multipartFile.getContentType());
        image.setContentContentType(multipartFile.getContentType());
        image.setTitle(image.getFileName());
        image.extention(multipartFile.getContentType());
        try {

            image.setContent(multipartFile.getBytes());
        } catch (IOException e) {
            log.warn("Error when get content for image: {}: {}", image.getFileName(), e);
        }

        // save Image
        Image newImage = imageRepository.save(image);
        log.info("Save image into database: {} - {}", newImage.getId(), newImage.getFileName());

        return Optional.of(newImage);
    }

}
