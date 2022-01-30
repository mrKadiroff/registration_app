package com.example.registration.models

import java.io.Serializable

class RegisterModel: Serializable {

    var id: Int? = null
    var rasm: String? = null
    var ism: String? = null
    var telefon: String? = null
    var mamlakat: String? = null
    var manzil: String? = null
    var parol: String? = null

    constructor()
    constructor(
        id: Int?,
        rasm: String?,
        ism: String?,
        telefon: String?,
        mamlakat: String?,
        manzil: String?,
        parol: String?
    ) {
        this.id = id
        this.rasm = rasm
        this.ism = ism
        this.telefon = telefon
        this.mamlakat = mamlakat
        this.manzil = manzil
        this.parol = parol
    }

    constructor(
        rasm: String?,
        ism: String?,
        telefon: String?,
        mamlakat: String?,
        manzil: String?,
        parol: String?
    ) {
        this.rasm = rasm
        this.ism = ism
        this.telefon = telefon
        this.mamlakat = mamlakat
        this.manzil = manzil
        this.parol = parol
    }


}