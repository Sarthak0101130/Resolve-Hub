package com.example.complain_management

data class AdminData(
    val email:String?= null,
    var name:String?= null,
    var number:String?= null,
    var organizationname:String?= null,
    var uid:List<String?>? = null,//This includes the list of user id under a admin
    var admin_services: List<String?>? = null,
)

data class UserData(
    var email:String?= null,
    var name:String?= null,
    var number:String?= null,
    var organizationname:String?= null,
    var age:String?=null,
    var flatNo:String?=null,
    var buildingNo:String?=null,
    var buildingName:String?=null,
    val adminId:String?=null,

)

data class Verification(
    val verificationId: String?=null,
    val timestamp:Long?=null,
    val verified:String?=null,
    var type:String?=null,
    var phone:String?=null
                                                                                       
)

data class UserComplain(
    var ComplainType: String?=null,
    var ComplainSubject: String?=null,
    var ComplainDescription: String?=null,
    var image:String?=null,
    var verified:String?="No",
)
