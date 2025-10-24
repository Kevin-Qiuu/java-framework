package com.bitejiuyeke.bitefileapi.feign;

import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitefileapi.domain.dto.FileUploadDTO;
import com.bitejiuyeke.bitefileapi.domain.vo.COSSignVO;
import com.bitejiuyeke.bitefileapi.domain.vo.FileVO;
import com.bitejiuyeke.bitefileapi.domain.vo.OSSSignVO;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@ConditionalOnProperty(name = "feign.bite-file.feignEnabled", havingValue = "true")
@FeignClient(contextId = "fileFeignClient", name = "bite-file", configuration = FileFeignClient.MultipartFileSupportConfig.class)
public interface FileFeignClient {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<FileVO> upload(@RequestPart("multipartFile") MultipartFile multipartFile,
                     @RequestParam("filePrefix") String filePrefix);

    /**
     * 为什么客户端直传
     * 在典型的服务端和客户端架构下，常见的文件上传方式是服务端代理上传：客户端将文件上传到业务服务器，然后业务服务器将文件上传到OSS。在这个过程中，一份数据需要在网络上传输两次，会造成网络资源的浪费，增加服务端的资源开销。为了解决这一问题，您可以在客户端直连OSS来完成文件上传，无需经过业务服务器中转。<br>
     * <br><br>
     * COS 获取签名授权，需给定上传的文件 key (filename)<br>
     * COS 与 OOS 不同，COS 授权只可授权上传文件 url 的签名，无法对整个目录或者整个 bucket 进行授权<br>
     * 所以上传的时候需要传递本次上传的文件名，用于获取上传文件的后缀名<br>
     * useUUIDFilename 表示是否通过 UUID 更改上传文件的文件名<br>
     *
     * @param filename        上传文件名
     * @param useUUIDFilename 上传文件名使用 UUID，不传递或者传递 false 则不使用
     * @return R<COSSignVO>
     */
    @GetMapping("/get_cos_sign")
    default R<COSSignVO> getCOSSign(String filename, Boolean useUUIDFilename) {
        return null;
    }


    /**
     * 获取 OOS 签名信息
     *
     * @return oos 签名信息
     */
    @GetMapping("get_oss_sign")
    default R<OSSSignVO> getOSSSign() {
        return null;
    }

    /**
     * 为 fileFeign 分配单独的对象编码器，因为 Jackson 无法对 MultipartFile 进行 json 编码，
     * 因此需要使用其他的编码器，此时就不会优先使用全局的 Jackson 编码器了，但是需要注意：
     * 一定要将其放在 feign 的内部，表示专门为这个 feign 分配的隔离编码器，同时需要在 @FeignClient 中添加
     * configuration 标明。
     */
    class MultipartFileSupportConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }

}
