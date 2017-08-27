package com.univreview.model.model_kotlin

/**
 * Created by DavidHa on 2017. 8. 27..
 */
data class Course(override var id : Long, val professor: Professor) : AbstractDataProvider(){
    override var name: String
        get() =  professor.name
        set(value) {professor.name = value}
}