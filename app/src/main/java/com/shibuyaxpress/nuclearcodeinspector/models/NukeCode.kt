package com.shibuyaxpress.nuclearcodeinspector.models

data class NukeCode(
    var nukeCode: String?,
    var name: String?,
    var description: String?,
    var image: String?,
    var url: String?
){
    constructor(nukeCode: String?,name: String?,description: String?,image: String?)
    :this(nukeCode,name,description,image, url = null) {
        this.nukeCode = nukeCode
        this.description = description
        this.image = image
        this.name = name
        this.url = "https://nhentai.net/g/${nukeCode}"
    }
}