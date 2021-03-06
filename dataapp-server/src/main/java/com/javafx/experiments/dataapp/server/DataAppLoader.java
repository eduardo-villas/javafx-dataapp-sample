/*
 * Copyright (c) 2008, 2011 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.javafx.experiments.dataapp.server;

import com.javafx.experiments.dataapp.model.*;
import com.javafx.experiments.dataapp.simulation.SalesSimulator;
import com.javafx.experiments.dataapp.simulation.persistance.InitialLoadEntityManagerProxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Calendar;
import java.util.Date;

/**
 * DataAppLoader - generates all entity information and historical data
 */
class DataAppLoader {

    static void loadAll(EntityManager entityManager) {
        EntityTransaction et = entityManager.getTransaction();
        et.begin();

        System.out.println("Loading discount rates");
        loadDiscountRate(entityManager);

        System.out.println("Loading regions");
        loadRegion(entityManager);

        System.out.println("Loading roles");
        loadRole(entityManager);

        System.out.println("Loading transmissions");
        loadTransmission(entityManager);

        System.out.println("Loading product types");
        loadProductType(entityManager);

        System.out.println("Loading engines");
        loadEngine(entityManager);

        System.out.println("Loading products");
        loadProduct(entityManager);
        et.commit();

        EntityManager specialEntityManager = new InitialLoadEntityManagerProxy(entityManager);
        SalesSimulator simulator = new SalesSimulator(specialEntityManager);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(year - 1, Calendar.JANUARY, 1, 0, 0, 0); // go back to beginning of year, 1 year ago
        System.out.println("Creating historical data...");
        simulator.run(cal.getTime(), new Date());
    }

    private static void loadDiscountRate(EntityManager entityManager) {
        DiscountRate dr1 = new DiscountRate('H');
        DiscountRate dr2 = new DiscountRate('M');
        DiscountRate dr3 = new DiscountRate('L');
        DiscountRate dr4 = new DiscountRate('N');

        dr1.setRate(0.08);
        dr2.setRate(0.04);
        dr3.setRate(0.02);
        dr4.setRate(0.00);

        entityManager.persist(dr1);
        entityManager.persist(dr2);
        entityManager.persist(dr3);
        entityManager.persist(dr4);
    }

    private static void loadRegion(EntityManager entityManager) {
        final String[] domesticNames = {"Northeast", "Mid-Atlantic", "South", "Mid-West", "Ark-La-Tex", "Southwest", "West", "Territories"};
        //Zones are based on ZIP code prefix:
        //see http://en.wikipedia.org/wiki/ZIP_code#Primary_State_Prefixes
        final int[] domesticStartZones = {1, 20, 30, 40, 70, 80, 90, 0};
        final int[] domesticEndZones = {19, 29, 39, 69, 79, 89, 99, 0};

        //these zones are arbitrary
        final String[] internationalNames = {"EEMEA", "Western Europe", "Asia Pacfic", "Americas"};
        final int[] internationalStartZones = {1, 26, 51, 76};
        final int[] internationalEndZones = {25, 50, 75, 99};

        for (int i = 0; i < domesticNames.length; i++) {
            Region r = new Region(i + 1, (short)0, domesticStartZones[i], domesticEndZones[i]);
            r.setName(domesticNames[i]);
            entityManager.persist(r);
        }
        for (int i = 0; i < internationalNames.length; i++) {
            Region r = new Region(domesticNames.length + i + 1, (short)1, internationalStartZones[i], internationalEndZones[i]);
            r.setName(internationalNames[i]);
            entityManager.persist(r);
        }
    }

    private static void loadRole(EntityManager entityManager) {
        Role r = new Role('E');
        r.setDescription("Sales Executive, National");
        entityManager.persist(r);

        r = new Role('D');
        r.setDescription("District Manager");
        entityManager.persist(r);

        r = new Role('T');
        r.setDescription("Sales, Territory");
        entityManager.persist(r);

        r = new Role('A');
        r.setDescription("Sales, Associate");
        entityManager.persist(r);
    }

    private static void loadEngine(EntityManager entityManager) {
        //I4
        Engine e0 = new Engine(1);
        e0.setCylinders((short)4);
        e0.setHybrid((short)0);
        e0.setInline((short)1);
        e0.setSupercharge((short)0);
        e0.setTurbocharge((short)0);
        e0.setVee((short)0);
        e0.setLitre(1.9);
        entityManager.persist(e0);

        Engine e1 = new Engine(2);
        e1.setCylinders((short)4);
        e1.setHybrid((short)0);
        e1.setInline((short)1);
        e1.setSupercharge((short)0);
        e1.setTurbocharge((short)0);
        e1.setVee((short)0);
        e1.setLitre(2.2);
        entityManager.persist(e1);

        Engine e2 = new Engine(3);
        e2.setCylinders((short)4);
        e2.setHybrid((short)0);
        e2.setInline((short)1);
        e2.setSupercharge((short)0);
        e2.setTurbocharge((short)1);
        e2.setVee((short)0);
        e2.setLitre(2.4);
        entityManager.persist(e2);

        //V6
        Engine e3 = new Engine(4);
        e3.setCylinders((short)6);
        e3.setHybrid((short)0);
        e3.setInline((short)0);
        e3.setSupercharge((short)0);
        e3.setTurbocharge((short)0);
        e3.setVee((short)1);
        e3.setLitre(2.4);
        entityManager.persist(e3);

        Engine e4 = new Engine(5);
        e4.setCylinders((short)6);
        e4.setHybrid((short)0);
        e4.setInline((short)0);
        e4.setSupercharge((short)0);
        e4.setTurbocharge((short)0);
        e4.setVee((short)1);
        e4.setLitre(3.4);
        entityManager.persist(e4);

        Engine e5 = new Engine(6);
        e5.setCylinders((short)6);
        e5.setHybrid((short)0);
        e5.setInline((short)0);
        e5.setSupercharge((short)0);
        e5.setTurbocharge((short)0);
        e5.setVee((short)1);
        e5.setLitre(4.2);
        entityManager.persist(e5);

        //v8
        Engine e6 = new Engine(7);
        e6.setCylinders((short)8);
        e6.setHybrid((short)0);
        e6.setInline((short)0);
        e6.setSupercharge((short)0);
        e6.setTurbocharge((short)0);
        e6.setVee((short)1);
        e6.setLitre(4.7);
        entityManager.persist(e6);

        Engine e7 = new Engine(8);
        e7.setCylinders((short)8);
        e7.setHybrid((short)0);
        e7.setInline((short)0);
        e7.setSupercharge((short)0);
        e7.setTurbocharge((short)0);
        e7.setVee((short)1);
        e7.setLitre(5.5);
        entityManager.persist(e7);

        Engine e8 = new Engine(9);
        e8.setCylinders((short)8);
        e8.setHybrid((short)0);
        e8.setInline((short)0);
        e8.setSupercharge((short)1);
        e8.setTurbocharge((short)0);
        e8.setVee((short)1);
        e8.setLitre(5.5);
        entityManager.persist(e8);

        Engine e9 = new Engine(10);
        e9.setCylinders((short)8);
        e9.setHybrid((short)0);
        e9.setInline((short)0);
        e9.setSupercharge((short)0);
        e9.setTurbocharge((short)0);
        e9.setVee((short)1);
        e9.setLitre(6.3);
        entityManager.persist(e9);

        Engine e10 = new Engine(11);
        e10.setCylinders((short)4);
        e10.setHybrid((short)1);
        e10.setInline((short)1);
        e10.setSupercharge((short)0);
        e10.setTurbocharge((short)0);
        e10.setVee((short)0);
        e10.setLitre(1.9);
        entityManager.persist(e10);
    }

    private static void loadTransmission(EntityManager entityManager) {
        Transmission t1 = new Transmission(1, "MAN");
        t1.setGears((short)5);

        Transmission t2 = new Transmission(2, "MAN");
        t2.setGears((short)6);

        Transmission t3 = new Transmission(3, "AUTO");
        t3.setGears((short)4);

        Transmission t4 = new Transmission(4, "AUTO");
        t4.setGears((short)5);

        Transmission t5 = new Transmission(5, "AUTO");
        t5.setGears((short)6);

        Transmission t6 = new Transmission(6, "CVT");

        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.persist(t3);
        entityManager.persist(t4);
        entityManager.persist(t5);
        entityManager.persist(t6);
    }

    private static void loadProductType(EntityManager entityManager) {
        ProductType pt0 = new ProductType(1);
        pt0.setClass1("Car");
        pt0.setSubclass("City");

        ProductType pt1 = new ProductType(2);
        pt1.setClass1("Car");
        pt1.setSubclass("Sub-Compact");

        ProductType pt2 = new ProductType(3);
        pt2.setClass1("Car");
        pt2.setSubclass("Compact");

        ProductType pt3 = new ProductType(4);
        pt3.setClass1("Car");
        pt3.setSubclass("Mid-Size");

        ProductType pt4 = new ProductType(5);
        pt4.setClass1("Car");
        pt4.setSubclass("Full-Size");

        ProductType pt5 = new ProductType(6);
        pt5.setClass1("SUV");
        pt5.setSubclass("Sub-Compact");

        ProductType pt6 = new ProductType(7);
        pt6.setClass1("SUV");
        pt6.setSubclass("Compact");

        ProductType pt7 = new ProductType(8);
        pt7.setClass1("SUV");
        pt7.setSubclass("Mid-Size");

        ProductType pt8 = new ProductType(9);
        pt8.setClass1("SUV");
        pt8.setSubclass("Full-Size");

        ProductType pt9 = new ProductType(10);
        pt9.setClass1("Van");
        pt9.setSubclass("Compact");

        ProductType pt10 = new ProductType(11);
        pt10.setClass1("Van");
        pt10.setSubclass("Full-Size");

        ProductType pt11 = new ProductType(12);
        pt11.setClass1("Truck");
        pt11.setSubclass("Sub-Compact");

        ProductType pt12 = new ProductType(13);
        pt12.setClass1("Truck");
        pt12.setSubclass("Compact");

        ProductType pt13 = new ProductType(14);
        pt13.setClass1("Truck");
        pt13.setSubclass("Full-Size");

        ProductType pt14 = new ProductType(15);
        pt14.setClass1("Truck");
        pt14.setSubclass("Over-Size");

        ProductType pt15 = new ProductType(16);
        pt15.setClass1("Specialty");
        pt15.setSubclass("Pony Car");

        entityManager.persist(pt0);
        entityManager.persist(pt1);
        entityManager.persist(pt2);
        entityManager.persist(pt3);
        entityManager.persist(pt4);
        entityManager.persist(pt5);
        entityManager.persist(pt6);
        entityManager.persist(pt7);
        entityManager.persist(pt8);
        entityManager.persist(pt9);
        entityManager.persist(pt10);
        entityManager.persist(pt11);
        entityManager.persist(pt12);
        entityManager.persist(pt13);
        entityManager.persist(pt14);
        entityManager.persist(pt15);
    }

    private static void loadProduct(EntityManager entityManager) {
        //ponies ---------------------------------------------------------------
        //v8 man
        Product pc0 = new Product(1);
        pc0.setCost(27000.00);
        pc0.setPrice(29999.98);
        pc0.setHeight(53.9);
        pc0.setWidth(73.7);
        pc0.setLength(186.8);
        pc0.setModelYear(2011);
        pc0.setName("Tarpan");

        ProductType pt = entityManager.find(ProductType.class, 16);
        Engine e = entityManager.find(Engine.class, 9);
        Transmission t = entityManager.find(Transmission.class, 2);
        pc0.setEngine(e);
        pc0.setProductType(pt);
        pc0.setTransmission(t);

        entityManager.persist(pc0);

        //v8 auto
        Product pc1 = new Product(2);
        pc1.setCost(26500.00);
        pc1.setPrice(29599.98);
        pc1.setHeight(53.9);
        pc1.setWidth(73.7);
        pc1.setLength(186.8);
        pc1.setModelYear(2011);
        pc1.setName("Tarpan");

        pt = entityManager.find(ProductType.class, 16);
        e = entityManager.find(Engine.class, 9);
        t = entityManager.find(Transmission.class, 5);
        pc1.setEngine(e);
        pc1.setProductType(pt);
        pc1.setTransmission(t);

        entityManager.persist(pc1);

        //v6 auto
        Product pc2 = new Product(3);
        pc2.setCost(23500.00);
        pc2.setPrice(24999.98);
        pc2.setHeight(53.9);
        pc2.setWidth(73.7);
        pc2.setLength(186.8);
        pc2.setModelYear(2011);
        pc2.setName("Tarpan");

        pt = entityManager.find(ProductType.class, 16);
        e = entityManager.find(Engine.class, 6);
        t = entityManager.find(Transmission.class, 4);
        pc2.setEngine(e);
        pc2.setProductType(pt);
        pc2.setTransmission(t);

        entityManager.persist(pc2);

        //v6 man
        Product pc3 = new Product(4);
        pc3.setCost(22500.00);
        pc3.setPrice(26599.98);
        pc3.setHeight(53.9);
        pc3.setWidth(73.7);
        pc3.setLength(186.8);
        pc3.setModelYear(2011);
        pc3.setName("Tarpan");

        pt = entityManager.find(ProductType.class, 16);
        e = entityManager.find(Engine.class, 9);
        t = entityManager.find(Transmission.class, 5);
        pc3.setEngine(e);
        pc3.setProductType(pt);
        pc3.setTransmission(t);

        entityManager.persist(pc3);

        //City Car --
        Product pc4 = new Product(5);
        pc4.setCost(12000.00);
        pc4.setPrice(14599.98);
        pc4.setHeight(63.9);
        pc4.setWidth(55.9);
        pc4.setLength(143.5);
        pc4.setModelYear(2011);
        pc4.setName("Tinie");

        pt = entityManager.find(ProductType.class, 1);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 1);
        pc4.setEngine(e);
        pc4.setProductType(pt);
        pc4.setTransmission(t);

        entityManager.persist(pc4);

        //Car - Sub compact-----------------------------------------------------

        Product pc5 = new Product(6);
        pc5.setCost(12500.00);
        pc5.setPrice(14999.98);
        pc5.setHeight(57.9);
        pc5.setWidth(65.9);
        pc5.setLength(168.5);
        pc5.setModelYear(2011);
        pc5.setName("Fete");

        pt = entityManager.find(ProductType.class, 2);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 3);
        pc5.setEngine(e);
        pc5.setProductType(pt);
        pc5.setTransmission(t);

        entityManager.persist(pc5);

        Product pc6 = new Product(7);
        pc6.setCost(12000.00);
        pc6.setPrice(14599.98);
        pc6.setHeight(57.9);
        pc6.setWidth(65.9);
        pc6.setLength(168.5);
        pc6.setModelYear(2011);
        pc6.setName("Fete");

        pt = entityManager.find(ProductType.class, 2);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 1);
        pc6.setEngine(e);
        pc6.setProductType(pt);
        pc6.setTransmission(t);

        entityManager.persist(pc6);

        //Car - compact -------------------------------------------------------_

        //engine 0
        Product pc7 = new Product(8);
        pc7.setCost(15400.00);
        pc7.setPrice(17299.98);
        pc7.setHeight(58.9);
        pc7.setWidth(71.9);
        pc7.setLength(176.2);
        pc7.setModelYear(2011);
        pc7.setName("Keskus");

        pt = entityManager.find(ProductType.class, 3);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 1);
        pc7.setEngine(e);
        pc7.setProductType(pt);
        pc7.setTransmission(t);

        entityManager.persist(pc7);

        Product pc8 = new Product(9);
        pc8.setCost(15800.00);
        pc8.setPrice(18199.98);
        pc8.setHeight(58.9);
        pc8.setWidth(71.9);
        pc8.setLength(176.2);
        pc8.setModelYear(2011);
        pc8.setName("Keskus");

        pt = entityManager.find(ProductType.class, 3);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 5);
        pc8.setEngine(e);
        pc8.setProductType(pt);
        pc8.setTransmission(t);

        entityManager.persist(pc8);

        //engine 1
        Product pc9 = new Product(10);
        pc9.setCost(15900.00);
        pc9.setPrice(17999.98);
        pc9.setHeight(58.9);
        pc9.setWidth(71.9);
        pc9.setLength(176.2);
        pc9.setModelYear(2011);
        pc9.setName("Keskus");

        pt = entityManager.find(ProductType.class, 3);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 1);
        pc9.setEngine(e);
        pc9.setProductType(pt);
        pc9.setTransmission(t);

        entityManager.persist(pc9);

        Product pc10 = new Product(11);
        pc10.setCost(16400.00);
        pc10.setPrice(18699.98);
        pc10.setHeight(58.9);
        pc10.setWidth(71.9);
        pc10.setLength(176.2);
        pc10.setModelYear(2011);
        pc10.setName("Keskus");

        pt = entityManager.find(ProductType.class, 3);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 5);
        pc10.setEngine(e);
        pc10.setProductType(pt);
        pc10.setTransmission(t);

        entityManager.persist(pc10);

        //engine 2 - sporty model
        Product pc11 = new Product(12);
        pc11.setCost(17400.00);
        pc11.setPrice(22599.98);
        pc11.setHeight(58.9);
        pc11.setWidth(71.9);
        pc11.setLength(176.2);
        pc11.setModelYear(2011);
        pc11.setName("Keskus");

        pt = entityManager.find(ProductType.class, 3);
        e = entityManager.find(Engine.class, 3);
        t = entityManager.find(Transmission.class, 2);
        pc11.setEngine(e);
        pc11.setProductType(pt);
        pc11.setTransmission(t);

        entityManager.persist(pc11);

        Product pc12 = new Product(13);
        pc12.setCost(18400.00);
        pc12.setPrice(24599.98);
        pc12.setHeight(58.9);
        pc12.setWidth(71.9);
        pc12.setLength(176.2);
        pc12.setModelYear(2011);
        pc12.setName("Keskus");

        pt = entityManager.find(ProductType.class, 3);
        e = entityManager.find(Engine.class, 3);
        t = entityManager.find(Transmission.class, 5);
        pc12.setEngine(e);
        pc12.setProductType(pt);
        pc12.setTransmission(t);

        entityManager.persist(pc12);

        //Car - Mid Size -------------------------------------------------------
        //i4 
        Product pc13 = new Product(14);
        pc13.setCost(17500.00);
        pc13.setPrice(20599.98);
        pc13.setHeight(56.8);
        pc13.setWidth(72.4);
        pc13.setLength(190.2);
        pc13.setModelYear(2011);
        pc13.setName("Centrum");

        pt = entityManager.find(ProductType.class, 4);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 5);
        pc13.setEngine(e);
        pc13.setProductType(pt);
        pc13.setTransmission(t);

        entityManager.persist(pc13);

        Product pc14 = new Product(15);
        pc14.setCost(17900.00);
        pc14.setPrice(19599.98);
        pc14.setHeight(56.8);
        pc14.setWidth(72.4);
        pc14.setLength(190.2);
        pc14.setModelYear(2011);
        pc14.setName("Centrum");

        pt = entityManager.find(ProductType.class, 4);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 2);
        pc14.setEngine(e);
        pc14.setProductType(pt);
        pc14.setTransmission(t);

        entityManager.persist(pc14);

        Product pc15 = new Product(16);
        pc15.setCost(18400.00);
        pc15.setPrice(21599.98);
        pc15.setHeight(56.8);
        pc15.setWidth(72.4);
        pc15.setLength(190.2);
        pc15.setModelYear(2011);
        pc15.setName("Centrum");

        pt = entityManager.find(ProductType.class, 4);
        e = entityManager.find(Engine.class, 11);
        t = entityManager.find(Transmission.class, 6);
        pc15.setEngine(e);
        pc15.setProductType(pt);
        pc15.setTransmission(t);

        entityManager.persist(pc15);

        //v6
        Product pc16 = new Product(17);
        pc16.setCost(24000.00);
        pc16.setPrice(26599.98);
        pc16.setHeight(56.8);
        pc16.setWidth(72.4);
        pc16.setLength(190.2);
        pc16.setModelYear(2011);
        pc16.setName("Centrum");

        pt = entityManager.find(ProductType.class, 4);
        e = entityManager.find(Engine.class, 4);
        t = entityManager.find(Transmission.class, 5);
        pc16.setEngine(e);
        pc16.setProductType(pt);
        pc16.setTransmission(t);

        entityManager.persist(pc16);

        //v6
        Product pc17 = new Product(18);
        pc17.setCost(23000.00);
        pc17.setPrice(26599.98);
        pc17.setHeight(56.8);
        pc17.setWidth(72.4);
        pc17.setLength(190.2);
        pc17.setModelYear(2011);
        pc17.setName("Centrum");

        pt = entityManager.find(ProductType.class, 4);
        e = entityManager.find(Engine.class, 5);
        t = entityManager.find(Transmission.class, 5);
        pc17.setEngine(e);
        pc17.setProductType(pt);
        pc17.setTransmission(t);

        entityManager.persist(pc17);

        //Car - Full Size ------------------------------------------------------

        //v6
        Product pc18 = new Product(19);
        pc18.setCost(22500.00);
        pc18.setPrice(25599.98);
        pc18.setHeight(60.8);
        pc18.setWidth(77.1);
        pc18.setLength(203.2);
        pc18.setModelYear(2011);
        pc18.setName("Stier");

        pt = entityManager.find(ProductType.class, 5);
        e = entityManager.find(Engine.class, 5);
        t = entityManager.find(Transmission.class, 5);
        pc18.setEngine(e);
        pc18.setProductType(pt);
        pc18.setTransmission(t);

        entityManager.persist(pc18);

        //v6
        Product pc19 = new Product(20);
        pc19.setCost(24000.00);
        pc19.setPrice(27599.98);
        pc19.setHeight(60.8);
        pc19.setWidth(77.1);
        pc19.setLength(203.2);
        pc19.setModelYear(2011);
        pc19.setName("Stier");

        pt = entityManager.find(ProductType.class, 5);
        e = entityManager.find(Engine.class, 6);
        t = entityManager.find(Transmission.class, 5);
        pc19.setEngine(e);
        pc19.setProductType(pt);
        pc19.setTransmission(t);

        entityManager.persist(pc19);

        //Utility Vehicle  - Sub Compact ----------------------------------------
        //e1
        Product pc20 = new Product(21);
        pc20.setCost(12000.00);
        pc20.setPrice(14599.98);
        pc20.setHeight(61.4);
        pc20.setWidth(72.0);
        pc20.setLength(171.9);
        pc20.setModelYear(2011);
        pc20.setName("Mosquito");

        pt = entityManager.find(ProductType.class, 6);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 1);
        pc20.setEngine(e);
        pc20.setProductType(pt);
        pc20.setTransmission(t);

        entityManager.persist(pc20);

        Product pc21 = new Product(22);
        pc21.setCost(12300.00);
        pc21.setPrice(14999.98);
        pc21.setHeight(61.4);
        pc21.setWidth(72.0);
        pc21.setLength(171.9);
        pc21.setModelYear(2011);
        pc21.setName("Mosquito");

        pt = entityManager.find(ProductType.class, 6);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 2);
        pc21.setEngine(e);
        pc21.setProductType(pt);
        pc21.setTransmission(t);

        entityManager.persist(pc21);

        Product pc22 = new Product(23);
        pc22.setCost(12800.00);
        pc22.setPrice(15399.98);
        pc22.setHeight(61.4);
        pc22.setWidth(72.0);
        pc22.setLength(171.9);
        pc22.setModelYear(2011);
        pc22.setName("Mosquito");

        pt = entityManager.find(ProductType.class, 6);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 5);
        pc22.setEngine(e);
        pc22.setProductType(pt);
        pc22.setTransmission(t);

        entityManager.persist(pc22);

        Product pc23 = new Product(24);
        pc23.setCost(12600.00);
        pc23.setPrice(15199.98);
        pc23.setHeight(61.4);
        pc23.setWidth(72.0);
        pc23.setLength(171.9);
        pc23.setModelYear(2011);
        pc23.setName("Mosquito");

        pt = entityManager.find(ProductType.class, 6);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 3);
        pc23.setEngine(e);
        pc23.setProductType(pt);
        pc23.setTransmission(t);

        entityManager.persist(pc23);

        //e2
        Product pc24 = new Product(25);
        pc24.setCost(13000.00);
        pc24.setPrice(15599.98);
        pc24.setHeight(61.4);
        pc24.setWidth(72.0);
        pc24.setLength(171.9);
        pc24.setModelYear(2011);
        pc24.setName("Mosquito");

        pt = entityManager.find(ProductType.class, 6);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 1);
        pc24.setEngine(e);
        pc24.setProductType(pt);
        pc24.setTransmission(t);

        entityManager.persist(pc24);

        Product pc25 = new Product(26);
        pc25.setCost(13500.00);
        pc25.setPrice(15999.98);
        pc25.setHeight(61.4);
        pc25.setWidth(72.0);
        pc25.setLength(171.9);
        pc25.setModelYear(2011);
        pc25.setName("Mosquito");

        pt = entityManager.find(ProductType.class, 6);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 2);
        pc25.setEngine(e);
        pc25.setProductType(pt);
        pc25.setTransmission(t);

        entityManager.persist(pc25);

        Product pc26 = new Product(27);
        pc26.setCost(12000.00);
        pc26.setPrice(14599.98);
        pc26.setHeight(61.4);
        pc26.setWidth(72.0);
        pc26.setLength(171.9);
        pc26.setModelYear(2011);
        pc26.setName("Mosquito");

        pt = entityManager.find(ProductType.class, 6);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 3);
        pc26.setEngine(e);
        pc26.setProductType(pt);
        pc26.setTransmission(t);

        entityManager.persist(pc26);

        Product pc27 = new Product(28);
        pc27.setCost(13700.00);
        pc27.setPrice(15899.98);
        pc27.setHeight(61.4);
        pc27.setWidth(72.0);
        pc27.setLength(171.9);
        pc27.setModelYear(2011);
        pc27.setName("Mosquito");

        pt = entityManager.find(ProductType.class, 6);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 5);
        pc27.setEngine(e);
        pc27.setProductType(pt);
        pc27.setTransmission(t);

        entityManager.persist(pc27);

        //Utility Vehicle - Compact --------------------------------------------
        Product pc28 = new Product(29);
        pc28.setCost(21500.00);
        pc28.setPrice(22599.98);
        pc28.setHeight(68.1);
        pc28.setWidth(72.0);
        pc28.setLength(175.1);
        pc28.setModelYear(2011);
        pc28.setName("Galaxia");

        pt = entityManager.find(ProductType.class, 7);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 5);
        pc28.setEngine(e);
        pc28.setProductType(pt);
        pc28.setTransmission(t);

        entityManager.persist(pc28);

        Product pc29 = new Product(30);
        pc29.setCost(20400.00);
        pc29.setPrice(22199.98);
        pc29.setHeight(68.1);
        pc29.setWidth(72.0);
        pc29.setLength(175.1);
        pc29.setModelYear(2011);
        pc29.setName("Galaxia");

        pt = entityManager.find(ProductType.class, 7);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 1);
        pc29.setEngine(e);
        pc29.setProductType(pt);
        pc29.setTransmission(t);

        entityManager.persist(pc29);

        Product pc30 = new Product(31);
        pc30.setCost(22000.00);
        pc30.setPrice(24599.98);
        pc30.setHeight(68.1);
        pc30.setWidth(72.0);
        pc30.setLength(175.1);
        pc30.setModelYear(2011);
        pc30.setName("Galaxia");

        pt = entityManager.find(ProductType.class, 7);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 3);
        pc30.setEngine(e);
        pc30.setProductType(pt);
        pc30.setTransmission(t);

        entityManager.persist(pc30);

        //second engine
        Product pc31 = new Product(32);
        pc31.setCost(22000.00);
        pc31.setPrice(25599.98);
        pc31.setHeight(68.1);
        pc31.setWidth(72.0);
        pc31.setLength(175.1);
        pc31.setModelYear(2011);
        pc31.setName("Galaxia");

        pt = entityManager.find(ProductType.class, 7);
        e = entityManager.find(Engine.class, 4);
        t = entityManager.find(Transmission.class, 5);
        pc31.setEngine(e);
        pc31.setProductType(pt);
        pc31.setTransmission(t);

        entityManager.persist(pc31);

        Product pc32 = new Product(33);
        pc32.setCost(22500.00);
        pc32.setPrice(25999.98);
        pc32.setHeight(68.1);
        pc32.setWidth(72.0);
        pc32.setLength(175.1);
        pc32.setModelYear(2011);
        pc32.setName("Galaxia");

        pt = entityManager.find(ProductType.class, 7);
        e = entityManager.find(Engine.class, 4);
        t = entityManager.find(Transmission.class, 3);
        pc32.setEngine(e);
        pc32.setProductType(pt);
        pc32.setTransmission(t);

        entityManager.persist(pc32);

        Product pc33 = new Product(34);
        pc33.setCost(21000.00);
        pc33.setPrice(24199.98);
        pc33.setHeight(68.1);
        pc33.setWidth(72.0);
        pc33.setLength(175.1);
        pc33.setModelYear(2011);
        pc33.setName("Galaxia");

        pt = entityManager.find(ProductType.class, 7);
        e = entityManager.find(Engine.class, 4);
        t = entityManager.find(Transmission.class, 1);
        pc33.setEngine(e);
        pc33.setProductType(pt);
        pc33.setTransmission(t);

        entityManager.persist(pc33);

        //third engine
        Product pc34 = new Product(35);
        pc34.setCost(25700.00);
        pc34.setPrice(28999.98);
        pc34.setHeight(68.1);
        pc34.setWidth(72.0);
        pc34.setLength(175.1);
        pc34.setModelYear(2011);
        pc34.setName("Galaxia");

        pt = entityManager.find(ProductType.class, 7);
        e = entityManager.find(Engine.class, 5);
        t = entityManager.find(Transmission.class, 5);
        pc34.setEngine(e);
        pc34.setProductType(pt);
        pc34.setTransmission(t);

        entityManager.persist(pc34);

        Product pc35 = new Product(36);
        pc35.setCost(24700.00);
        pc35.setPrice(26599.98);
        pc35.setHeight(68.1);
        pc35.setWidth(72.0);
        pc35.setLength(175.1);
        pc35.setModelYear(2011);
        pc35.setName("Galaxia");

        pt = entityManager.find(ProductType.class, 7);
        e = entityManager.find(Engine.class, 5);
        t = entityManager.find(Transmission.class, 1);
        pc35.setEngine(e);
        pc35.setProductType(pt);
        pc35.setTransmission(t);

        entityManager.persist(pc35);

        Product pc36 = new Product(37);
        pc36.setCost(25300.00);
        pc36.setPrice(27899.98);
        pc36.setHeight(68.1);
        pc36.setWidth(72.0);
        pc36.setLength(175.1);
        pc36.setModelYear(2011);
        pc36.setName("Galaxia");

        pt = entityManager.find(ProductType.class, 7);
        e = entityManager.find(Engine.class, 5);
        t = entityManager.find(Transmission.class, 3);
        pc36.setEngine(e);
        pc36.setProductType(pt);
        pc36.setTransmission(t);

        entityManager.persist(pc36);

        //Utility Vehicle - Mid Size -------------------------------------------
        //first engine
        Product pc37 = new Product(38);
        pc37.setCost(24000.00);
        pc37.setPrice(26899.98);
        pc37.setHeight(72.1);
        pc37.setWidth(73.9);
        pc37.setLength(193.9);
        pc37.setModelYear(2011);
        pc37.setName("Hudson");

        pt = entityManager.find(ProductType.class, 8);
        e = entityManager.find(Engine.class, 6);
        t = entityManager.find(Transmission.class, 4);
        pc37.setEngine(e);
        pc37.setProductType(pt);
        pc37.setTransmission(t);

        entityManager.persist(pc37);

        Product pc38 = new Product(39);
        pc38.setCost(24500.00);
        pc38.setPrice(27499.98);
        pc38.setHeight(72.1);
        pc38.setWidth(73.9);
        pc38.setLength(193.9);
        pc38.setModelYear(2011);
        pc38.setName("Hudson");

        pt = entityManager.find(ProductType.class, 8);
        e = entityManager.find(Engine.class, 6);
        t = entityManager.find(Transmission.class, 5);
        pc38.setEngine(e);
        pc38.setProductType(pt);
        pc38.setTransmission(t);

        entityManager.persist(pc38);

        //second engine
        Product pc39 = new Product(40);
        pc39.setCost(25000.00);
        pc39.setPrice(29899.98);
        pc39.setHeight(72.1);
        pc39.setWidth(73.9);
        pc39.setLength(193.9);
        pc39.setModelYear(2011);
        pc39.setName("Hudson");

        pt = entityManager.find(ProductType.class, 8);
        e = entityManager.find(Engine.class, 7);
        t = entityManager.find(Transmission.class, 4);
        pc39.setEngine(e);
        pc39.setProductType(pt);
        pc39.setTransmission(t);

        entityManager.persist(pc39);

        Product pc40 = new Product(41);
        pc40.setCost(28600.00);
        pc40.setPrice(32799.98);
        pc40.setHeight(72.1);
        pc40.setWidth(73.9);
        pc40.setLength(193.9);
        pc40.setModelYear(2011);
        pc40.setName("Hudson");

        pt = entityManager.find(ProductType.class, 8);
        e = entityManager.find(Engine.class, 7);
        t = entityManager.find(Transmission.class, 5);
        pc40.setEngine(e);
        pc40.setProductType(pt);
        pc40.setTransmission(t);

        entityManager.persist(pc40);

        //UV - Full Size -------------------------------------------------------
        Product pc41 = new Product(42);
        pc41.setCost(35800.00);
        pc41.setPrice(37999.98);
        pc41.setHeight(72.1);
        pc41.setWidth(77.9);
        pc41.setLength(206.8);
        pc41.setModelYear(2011);
        pc41.setName("Magellan");

        pt = entityManager.find(ProductType.class, 9);
        e = entityManager.find(Engine.class, 8);
        t = entityManager.find(Transmission.class, 5);
        pc41.setEngine(e);
        pc41.setProductType(pt);
        pc41.setTransmission(t);

        entityManager.persist(pc41);

        //Truck sub-compact ----------------------------------------------------
        Product pc42 = new Product(43);
        pc42.setCost(12000.00);
        pc42.setPrice(14599.98);
        pc42.setHeight(57.5);
        pc42.setWidth(64.1);
        pc42.setLength(167.9);
        pc42.setModelYear(2011);
        pc42.setName("Hornet");

        pt = entityManager.find(ProductType.class, 12);
        e = entityManager.find(Engine.class, 1);
        t = entityManager.find(Transmission.class, 1);
        pc42.setEngine(e);
        pc42.setProductType(pt);
        pc42.setTransmission(t);

        entityManager.persist(pc42);

        Product pc43 = new Product(44);
        pc43.setCost(12500.00);
        pc43.setPrice(14999.98);
        pc43.setHeight(57.5);
        pc43.setWidth(64.1);
        pc43.setLength(167.9);
        pc43.setModelYear(2011);
        pc43.setName("Hornet");

        pt = entityManager.find(ProductType.class, 12);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 1);
        pc43.setEngine(e);
        pc43.setProductType(pt);
        pc43.setTransmission(t);

        entityManager.persist(pc43);

        //Truck - Compact ------------------------------------------------------
        //i4
        Product pc44 = new Product(45);
        pc44.setCost(15000.00);
        pc44.setPrice(17999.98);
        pc44.setHeight(67.5);
        pc44.setWidth(70.3);
        pc44.setLength(200.9);
        pc44.setModelYear(2011);
        pc44.setName("Strider");

        pt = entityManager.find(ProductType.class, 13);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 1);
        pc44.setEngine(e);
        pc44.setProductType(pt);
        pc44.setTransmission(t);

        entityManager.persist(pc44);

        //i4
        Product pc45 = new Product(46);
        pc45.setCost(15500.00);
        pc45.setPrice(18599.98);
        pc45.setHeight(67.5);
        pc45.setWidth(70.3);
        pc45.setLength(200.9);
        pc45.setModelYear(2011);
        pc45.setName("Strider");

        pt = entityManager.find(ProductType.class, 13);
        e = entityManager.find(Engine.class, 2);
        t = entityManager.find(Transmission.class, 1);
        pc45.setEngine(e);
        pc45.setProductType(pt);
        pc45.setTransmission(t);

        entityManager.persist(pc45);

        //v6-1
        Product pc46 = new Product(47);
        pc46.setCost(15900.00);
        pc46.setPrice(19699.98);
        pc46.setHeight(67.5);
        pc46.setWidth(70.3);
        pc46.setLength(200.9);
        pc46.setModelYear(2011);
        pc46.setName("Strider");

        pt = entityManager.find(ProductType.class, 13);
        e = entityManager.find(Engine.class, 5);
        t = entityManager.find(Transmission.class, 1);
        pc46.setEngine(e);
        pc46.setProductType(pt);
        pc46.setTransmission(t);

        entityManager.persist(pc46);

        //v6 -2
        Product pc47 = new Product(48);
        pc47.setCost(17800.00);
        pc47.setPrice(22199.98);
        pc47.setHeight(67.5);
        pc47.setWidth(70.3);
        pc47.setLength(200.9);
        pc47.setModelYear(2011);
        pc47.setName("Strider");

        pt = entityManager.find(ProductType.class, 13);
        e = entityManager.find(Engine.class, 6);
        t = entityManager.find(Transmission.class, 1);
        pc47.setEngine(e);
        pc47.setProductType(pt);
        pc47.setTransmission(t);

        entityManager.persist(pc47);

        //Truck - Full size ----------------------------------------------------
        //first engine
        Product pc48 = new Product(49);
        pc48.setCost(20000.00);
        pc48.setPrice(22899.98);
        pc48.setHeight(74.5);
        pc48.setWidth(79.3);
        pc48.setLength(220.9);
        pc48.setModelYear(2011);
        pc48.setName("Server");

        pt = entityManager.find(ProductType.class, 14);
        e = entityManager.find(Engine.class, 6);
        t = entityManager.find(Transmission.class, 3);
        pc48.setEngine(e);
        pc48.setProductType(pt);
        pc48.setTransmission(t);

        entityManager.persist(pc48);

        Product pc49 = new Product(50);
        pc49.setCost(20500.00);
        pc49.setPrice(23499.98);
        pc49.setHeight(74.5);
        pc49.setWidth(79.3);
        pc49.setLength(220.9);
        pc49.setModelYear(2011);
        pc49.setName("Server");

        pt = entityManager.find(ProductType.class, 14);
        e = entityManager.find(Engine.class, 6);
        t = entityManager.find(Transmission.class, 5);
        pc49.setEngine(e);
        pc49.setProductType(pt);
        pc49.setTransmission(t);

        entityManager.persist(pc49);
        //second engine
        Product pc50 = new Product(51);
        pc50.setCost(21000.00);
        pc50.setPrice(24899.98);
        pc50.setHeight(74.5);
        pc50.setWidth(79.3);
        pc50.setLength(220.9);
        pc50.setModelYear(2011);
        pc50.setName("Server");

        pt = entityManager.find(ProductType.class, 14);
        e = entityManager.find(Engine.class, 7);
        t = entityManager.find(Transmission.class, 3);
        pc50.setEngine(e);
        pc50.setProductType(pt);
        pc50.setTransmission(t);

        entityManager.persist(pc50);

        Product pc51 = new Product(52);
        pc51.setCost(21500.00);
        pc51.setPrice(25199.98);
        pc51.setHeight(74.5);
        pc51.setWidth(79.3);
        pc51.setLength(220.9);
        pc51.setModelYear(2011);
        pc51.setName("Server");

        pt = entityManager.find(ProductType.class, 14);
        e = entityManager.find(Engine.class, 7);
        t = entityManager.find(Transmission.class, 5);
        pc51.setEngine(e);
        pc51.setProductType(pt);
        pc51.setTransmission(t);

        entityManager.persist(pc51);

        //third engine
        Product pc52 = new Product(53);
        pc52.setCost(22000.00);
        pc52.setPrice(25899.98);
        pc52.setHeight(74.5);
        pc52.setWidth(79.3);
        pc52.setLength(220.9);
        pc52.setModelYear(2011);
        pc52.setName("Server");

        pt = entityManager.find(ProductType.class, 14);
        e = entityManager.find(Engine.class, 8);
        t = entityManager.find(Transmission.class, 3);
        pc52.setEngine(e);
        pc52.setProductType(pt);
        pc52.setTransmission(t);

        entityManager.persist(pc52);

        Product pc53 = new Product(54);
        pc53.setCost(22500.00);
        pc53.setPrice(26199.98);
        pc53.setHeight(74.5);
        pc53.setWidth(79.3);
        pc53.setLength(220.9);
        pc53.setModelYear(2011);
        pc53.setName("Server");

        pt = entityManager.find(ProductType.class, 14);
        e = entityManager.find(Engine.class, 8);
        t = entityManager.find(Transmission.class, 5);
        pc53.setEngine(e);
        pc53.setProductType(pt);
        pc53.setTransmission(t);

        entityManager.persist(pc53);

        //fourth engine
        Product pc54 = new Product(55);
        pc54.setCost(23000.00);
        pc54.setPrice(26899.98);
        pc54.setHeight(74.5);
        pc54.setWidth(79.3);
        pc54.setLength(220.9);
        pc54.setModelYear(2011);
        pc54.setName("Server");

        pt = entityManager.find(ProductType.class, 14);
        e = entityManager.find(Engine.class, 10);
        t = entityManager.find(Transmission.class, 3);
        pc54.setEngine(e);
        pc54.setProductType(pt);
        pc54.setTransmission(t);

        entityManager.persist(pc54);

        Product pc55 = new Product(56);
        pc55.setCost(23500.00);
        pc55.setPrice(27299.98);
        pc55.setHeight(74.5);
        pc55.setWidth(79.3);
        pc55.setLength(220.9);
        pc55.setModelYear(2011);
        pc55.setName("Server");

        pt = entityManager.find(ProductType.class, 14);
        e = entityManager.find(Engine.class, 10);
        t = entityManager.find(Transmission.class, 5);
        pc55.setEngine(e);
        pc55.setProductType(pt);
        pc55.setTransmission(t);

        entityManager.persist(pc55);

        //Truck - Oversized ----------------------------------------------------
        //engine 1
        Product pc56 = new Product(57);
        pc56.setCost(28000.00);
        pc56.setPrice(29999.98);
        pc56.setHeight(76.0);
        pc56.setWidth(81.8);
        pc56.setLength(241.3);
        pc56.setModelYear(2011);
        pc56.setName("Grand Server");

        pt = entityManager.find(ProductType.class, 15);
        e = entityManager.find(Engine.class, 8);
        t = entityManager.find(Transmission.class, 1);
        pc56.setEngine(e);
        pc56.setProductType(pt);
        pc56.setTransmission(t);

        entityManager.persist(pc56);

        Product pc57 = new Product(58);
        pc57.setCost(28500.00);
        pc57.setPrice(30999.98);
        pc57.setHeight(76.0);
        pc57.setWidth(81.8);
        pc57.setLength(241.3);
        pc57.setModelYear(2011);
        pc57.setName("Grand Server");

        pt = entityManager.find(ProductType.class, 15);
        e = entityManager.find(Engine.class, 8);
        t = entityManager.find(Transmission.class, 2);
        pc57.setEngine(e);
        pc57.setProductType(pt);
        pc57.setTransmission(t);

        entityManager.persist(pc57);

        Product pc58 = new Product(59);
        pc58.setCost(29000.00);
        pc58.setPrice(31999.98);
        pc58.setHeight(76.0);
        pc58.setWidth(81.8);
        pc58.setLength(241.3);
        pc58.setModelYear(2011);
        pc58.setName("Grand Server");

        pt = entityManager.find(ProductType.class, 15);
        e = entityManager.find(Engine.class, 8);
        t = entityManager.find(Transmission.class, 3);
        pc58.setEngine(e);
        pc58.setProductType(pt);
        pc58.setTransmission(t);

        entityManager.persist(pc58);

        Product pc59 = new Product(60);
        pc59.setCost(29500.00);
        pc59.setPrice(32999.98);
        pc59.setHeight(76.0);
        pc59.setWidth(81.8);
        pc59.setLength(241.3);
        pc59.setModelYear(2011);
        pc59.setName("Grand Server");

        pt = entityManager.find(ProductType.class, 15);
        e = entityManager.find(Engine.class, 8);
        t = entityManager.find(Transmission.class, 4);
        pc59.setEngine(e);
        pc59.setProductType(pt);
        pc59.setTransmission(t);

        entityManager.persist(pc59);

        //engine 2
        Product pc60 = new Product(61);
        pc60.setCost(32000.00);
        pc60.setPrice(34599.98);
        pc60.setHeight(76.0);
        pc60.setWidth(81.8);
        pc60.setLength(241.3);
        pc60.setModelYear(2011);
        pc60.setName("Grand Server");

        pt = entityManager.find(ProductType.class, 15);
        e = entityManager.find(Engine.class, 10);
        t = entityManager.find(Transmission.class, 1);
        pc60.setEngine(e);
        pc60.setProductType(pt);
        pc60.setTransmission(t);

        entityManager.persist(pc60);

        Product pc61 = new Product(62);
        pc61.setCost(32500.00);
        pc61.setPrice(35999.98);
        pc61.setHeight(76.0);
        pc61.setWidth(81.8);
        pc61.setLength(241.3);
        pc61.setModelYear(2011);
        pc61.setName("Grand Server");

        pt = entityManager.find(ProductType.class, 15);
        e = entityManager.find(Engine.class, 10);
        t = entityManager.find(Transmission.class, 2);
        pc61.setEngine(e);
        pc61.setProductType(pt);
        pc61.setTransmission(t);

        entityManager.persist(pc61);

        Product pc62 = new Product(63);
        pc62.setCost(33000.00);
        pc62.setPrice(35599.98);
        pc62.setHeight(76.0);
        pc62.setWidth(81.8);
        pc62.setLength(241.3);
        pc62.setModelYear(2011);
        pc62.setName("Grand Server");

        pt = entityManager.find(ProductType.class, 15);
        e = entityManager.find(Engine.class, 10);
        t = entityManager.find(Transmission.class, 3);
        pc62.setEngine(e);
        pc62.setProductType(pt);
        pc62.setTransmission(t);

        entityManager.persist(pc62);

        Product pc63 = new Product(64);
        pc63.setCost(33500.00);
        pc63.setPrice(36999.98);
        pc63.setHeight(76.0);
        pc63.setWidth(81.8);
        pc63.setLength(241.3);
        pc63.setModelYear(2011);
        pc63.setName("Grand Server");

        pt = entityManager.find(ProductType.class, 15);
        e = entityManager.find(Engine.class, 10);
        t = entityManager.find(Transmission.class, 4);
        pc63.setEngine(e);
        pc63.setProductType(pt);
        pc63.setTransmission(t);

        entityManager.persist(pc63);
    }
}
