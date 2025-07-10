package com.example.templates.components

import kotlinx.html.*

fun FlowContent.dosenNavbar() {
    nav("navbar") {
        a(href = "/dosen", classes = "nav-link") { +"Dashboard" }
        a(href = "/dosen/nilai", classes = "nav-link") { +"Nilai" }
        a(href = "/dosen/bimbingan", classes = "nav-link") { +"Bimbingan" }
        a(href = "/logout", classes = "nav-link") { +"Logout" }
    }
}