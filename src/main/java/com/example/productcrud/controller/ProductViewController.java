package com.example.productcrud.controller;

import com.example.productcrud.model.Product;
import com.example.productcrud.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService service;

    public ProductViewController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = service.getAllProducts();
        model.addAttribute("prod", products);
        return "products";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    @PostMapping
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("photoFile") MultipartFile photoFile) throws IOException {

        if (!photoFile.isEmpty()) {
            String uploadDir = "uploads/";
            String fileName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, photoFile.getBytes());

            product.setPhoto(fileName); // Save filename to DB
        }

        service.saveProduct(product);
        return "redirect:/products";
    }



    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = service.getProductById(id);
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/update")
    public String updateProduct(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam Double price,
                                @RequestParam("photoFile") MultipartFile photoFile,
                                @RequestParam String oldPhoto) throws IOException {

        String photo = oldPhoto;

        // If a new file is selected
        if (!photoFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
            Path path = Paths.get("uploads/", fileName);
            Files.write(path, photoFile.getBytes());
            photo = fileName;
        }

        Product updated = Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .photo(photo)
                .build();

        service.updateProduct(id, updated);
        return "redirect:/products";
    }


}
