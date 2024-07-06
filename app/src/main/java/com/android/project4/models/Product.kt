package com.android.project4.models

import java.util.UUID

data class Product(
    var productRandomId : String? = null,
    var productTitle : String? = null,
    var productDescription : String? = null,
    var productPrice : Int? = null,
    var productStock : Int? = null,
    var productCategory : String? = null,
    var productScientificName : String? = null,
    var productSpecies  : String? = null,
    var productFamily  : String? = null,
    var productLight : String? = null,
    var productWater : String? = null,
    var productSoil  : String? = null,
    var productTemperature  : String? = null,
    var productPests : String? = null,
    var productToxicity : String? = null,
    var productFact1 : String? = null,
    var productFact2 : String? = null,
    var itemCount: Int? = null,
    var adminUid: String? = null,
    var productImageUris: ArrayList<String?>? = null,

)
