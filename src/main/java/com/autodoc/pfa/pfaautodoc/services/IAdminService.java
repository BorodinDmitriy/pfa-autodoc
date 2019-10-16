package com.autodoc.pfa.pfaautodoc.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface IAdminService {
    Boolean setImageSign(MultipartFile imageFile);
}
