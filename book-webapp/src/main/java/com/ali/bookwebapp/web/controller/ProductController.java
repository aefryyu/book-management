package com.ali.bookwebapp.web.controller;

import com.ali.bookwebapp.clients.catalog.CatalogServiceClient;
import com.ali.bookwebapp.clients.catalog.PageResult;
import com.ali.bookwebapp.clients.catalog.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final CatalogServiceClient catalogService;

    public ProductController(CatalogServiceClient catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public String index() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String showProductsPage(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("pageNo", page);
        return "products";
    }

    @GetMapping("/api/products")
    @ResponseBody
    public PageResult<Product> products(@RequestParam(name = "page", defaultValue = "1") int page) {
        log.info("Fetching products for page : {}", page);
        return catalogService.getProducts(page);
    }
}
