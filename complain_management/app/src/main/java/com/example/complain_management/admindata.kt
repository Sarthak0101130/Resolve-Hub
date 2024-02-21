package com.example.complain_management

data class admindata(
    var email:String?= null,
    var name:String?= null,
    var number:String?= null,
    var organizationname:String?= null,
    var uid:String?=null,
    var admin_services: List<String?>? = null,
    var type:String?=null,
    var age:String?=null,
    var flatNo:String?=null,
    var buildingNo:String?=null,
    var buildingName:String?=null,
    val adminId:String?=null,
    val verificationId:VerificationId?=null,
)

data class VerificationId(
    val verificationId: String?=null,
    val timestamp:Long?=null,
                                                                                       
)