package com.example.tenantlab

class Apartment(
    val apartmentNumber: Int,                                // The apartment's number, e.g. 101
    val tenants: MutableList<Tenant> = mutableListOf()       // A list of tenants; starts empty
    // MutableList means we are allowed to add and remove items after it is created.
) {

    // Adds one tenant to this apartment's list.
    fun addTenant(tenant: Tenant) {
        tenants.add(tenant)   // Put the tenant at the end of the list
    }

    // Prints the details of every tenant in this apartment.
    fun showTenants() {
        println("Apartment: $apartmentNumber")

        // A for loop visits each tenant in the list, one at a time.
        for (tenant in tenants) {
            println("Tenant: ${tenant.name}")
            // reading rentAmount runs the custom getter, hence
            // "Rent amount accessed." being printed each time.
            println("Rent: ${tenant.rentAmount}")
            println("Rent paid: ${tenant.isPaid}")
        }
    }
}