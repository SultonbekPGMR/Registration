package com.model

class User {
    var id:Int? = null
    var fullName:String? = null
    var number:String? = null
    var whichCountry:Int? = null
    var address:String? = null
    var password:String? = null
    var image:String? = null

    constructor(
        id: Int?,
        fullName: String?,
        number: String?,
        whichCountry: Int?,
        address: String?,
        password: String?,
        image: String?
    ) {
        this.id = id
        this.fullName = fullName
        this.number = number
        this.whichCountry = whichCountry
        this.address = address
        this.password = password
        this.image = image
    }

    constructor(
        fullName: String?,
        number: String?,
        whichCountry: Int?,
        address: String?,
        password: String?,
        image: String?
    ) {
        this.fullName = fullName
        this.number = number
        this.whichCountry = whichCountry
        this.address = address
        this.password = password
        this.image = image
    }


}
