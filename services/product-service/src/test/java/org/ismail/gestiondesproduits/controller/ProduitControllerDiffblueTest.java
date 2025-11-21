//package org.ismail.gestiondesproduits.controller;
//
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.ArrayList;
//
//import org.ismail.gestiondesproduits.model.Produit;
//import org.ismail.gestiondesproduits.service.ProduitService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.aot.DisabledInAotMode;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@ContextConfiguration(classes = {ProduitController.class})
//@DisabledInAotMode
//@ExtendWith(SpringExtension.class)
//class ProduitControllerDiffblueTest {
//    @Autowired
//    private ProduitController produitController;
//
//    @MockitoBean
//    private ProduitService produitService;
//
//    /**
//     * Test {@link ProduitController#creat(Produit)}.
//     *
//     * <p>Method under test: {@link ProduitController#creat(Produit)}
//     */
//    @Test
//    @DisplayName("Test creat(Produit)")
//    @Tag("MaintainedByDiffblue")
//    void testCreat() throws Exception {
//        // Arrange
//        Produit produit = new Produit();
//        produit.setDescription("The characteristics of someone or something");
//        produit.setId(1L);
//        produit.setName("Name");
//        produit.setQuantity(1);
//        produit.setUnitPrice(10.0d);
//        when(produitService.save(Mockito.<Produit>any())).thenReturn(produit);
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/products");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(produitController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(
//                        content()
//                                .string(
//                                        "{\"id\":1,\"name\":\"Name\",\"unitPrice\":10.0,\"description\":\"The characteristics of someone or something\","
//                                                + "\"quantity\":1}"));
//    }
//
//    /**
//     * Test {@link ProduitController#findById(Long)}.
//     *
//     * <p>Method under test: {@link ProduitController#findById(Long)}
//     */
//    @Test
//    @DisplayName("Test findById(Long)")
//    @Tag("MaintainedByDiffblue")
//    void testFindById() throws Exception {
//        // Arrange
//        Produit produit = new Produit();
//        produit.setDescription("The characteristics of someone or something");
//        produit.setId(1L);
//        produit.setName("Name");
//        produit.setQuantity(1);
//        produit.setUnitPrice(10.0d);
//        when(produitService.findById(Mockito.<Long>any())).thenReturn(produit);
//
//        MockHttpServletRequestBuilder requestBuilder =
//                MockMvcRequestBuilders.get("/api/v1/products/42").param("id", String.valueOf(1L));
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(produitController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(
//                        content()
//                                .string(
//                                        "{\"id\":1,\"name\":\"Name\",\"unitPrice\":10.0,\"description\":\"The characteristics of someone or something\","
//                                                + "\"quantity\":1}"));
//    }
//
//    /**
//     * Test {@link ProduitController#findAll()}.
//     *
//     * <p>Method under test: {@link ProduitController#findAll()}
//     */
//    @Test
//    @DisplayName("Test findAll()")
//    @Tag("MaintainedByDiffblue")
//    void testFindAll() throws Exception {
//        // Arrange
//        when(produitService.findAllProduits()).thenReturn(new ArrayList<>());
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/products");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(produitController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(content().string("[]"));
//    }
//
//    /**
//     * Test {@link ProduitController#delete(Produit)}.
//     *
//     * <p>Method under test: {@link ProduitController#delete(Produit)}
//     */
//    @Test
//    @DisplayName("Test delete(Produit)")
//    @Tag("MaintainedByDiffblue")
//    void testDelete() throws Exception {
//        // Arrange
//        doNothing().when(produitService).delete(Mockito.<Produit>any());
//
//        MockHttpServletRequestBuilder requestBuilder =
//                MockMvcRequestBuilders.delete("/api/v1/products");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(produitController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(status().isOk());
//    }
//
//    /**
//     * Test {@link ProduitController#update(Produit)}.
//     *
//     * <p>Method under test: {@link ProduitController#update(Produit)}
//     */
//    @Test
//    @DisplayName("Test update(Produit)")
//    @Tag("MaintainedByDiffblue")
//    void testUpdate() throws Exception {
//        // Arrange
//        Produit produit = new Produit();
//        produit.setDescription("The characteristics of someone or something");
//        produit.setId(1L);
//        produit.setName("Name");
//        produit.setQuantity(1);
//        produit.setUnitPrice(10.0d);
//        when(produitService.update(Mockito.<Produit>any())).thenReturn(produit);
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/products");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(produitController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(
//                        content()
//                                .string(
//                                        "{\"id\":1,\"name\":\"Name\",\"unitPrice\":10.0,\"description\":\"The characteristics of someone or something\","
//                                                + "\"quantity\":1}"));
//    }
//}
