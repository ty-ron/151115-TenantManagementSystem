package com.example.tenantlab

// Main.kt
// The entry point of the program. Kotlin starts running code inside main().

fun main() {

    // ===================== TASK 1 & 2: Objects and Constructor =====================

    println("===== Tasks 1 & 2: Creating tenants and paying rent =====")

    // Create two tenant objects, giving each one its basic details via the constructor
    val tenant1 = Tenant("John", 101, 15000.0)
    val tenant2 = Tenant("Mary", 102, 18000.0)

    // Only John pays his rent
    tenant1.payRent()

    // Print the payment status of both tenants
    println("${tenant1.name} paid? ${tenant1.isPaid}")   // true
    println("${tenant2.name} paid? ${tenant2.isPaid}")   // false

    // THINK ABOUT IT (Task 1):
    // Why can two objects from the same Tenant class have different names,
    // apartment numbers and payment statuses?
    // -> The class is only a blueprint. It says whatever properties a tenant has, but
    //    each object created from it gets its own copy of those properties in memory.
    //    Changing tenant1's isPaid does not touch tenant2's, so each object keeps
    //    its own values.

    // THINK ABOUT IT (Task 2):
    // What is the advantage of passing information through a constructor?
    // -> The object is complete and valid from the moment it is created. We do not
    //    need to create an empty object and fill in each property afterwards, so
    //    the code is shorter and cleaner, and we cannot forget to set any required details.

    // ===================== TASK 3 & 4: Setter and Getter =====================

    println()
    println("===== Tasks 3 & 4: Setter validation and getter =====")

    // Create a tenant with a valid rent amount
    val tenant3 = Tenant("Peter", 103, 20000.0)

    // Try to change the rent to a negative value. The setter should reject it.
    println("Trying to set rent to -500...")
    tenant3.rentAmount = -500.0          // Prints "Rent amount cannot be negative."

    // Print the rent. Reading it runs the getter (prints "Rent amount accessed.").
    // The result should still be 20000.0, which proves the negative value was rejected.
    println(tenant3.rentAmount)

    // A valid change (0 or more) is accepted:
    tenant3.rentAmount = 22000.0
    println(tenant3.rentAmount)          // Now prints 22000.0

    // THINK ABOUT IT (Task 3):
    // Why validate a value before storing it in an object?
    // -> To stop bad data (e.g, negative rent) from ever entering the system.
    //    If wrong values get in, calculations and reports break later and bugs are
    //    hard to trace. Checking in the setter protects the object in one place.

    // THINK ABOUT IT (Task 4):
    // When is the getter executed?
    // -> Every time the property is read, e.g. println(tenant3.rentAmount) or
    //    when we use it inside another function such as showTenants().
    // Getting vs setting a property:
    // -> GETTING reads the current value (tenant.rentAmount) and runs the getter.
    //    SETTING writes a new value (tenant.rentAmount = 5000.0) and runs the setter.

    // ===================== TASK 5, 6 & 7: Apartment and Composition =====================

    println()
    println("===== Tasks 5, 6 & 7: Apartment with tenants =====")

    // Create an Apartment object (number 101, tenants list starts empty)
    val apartment = Apartment(101)

    // Create two new tenants for this apartment
    val john = Tenant("John", 101, 15000.0)
    val mary = Tenant("Mary", 101, 18000.0)

    // Add both tenants to the apartment's list
    apartment.addTenant(john)
    apartment.addTenant(mary)

    // John pays, Mary does not
    john.payRent()

    // Verify both tenants are stored: the list size should be 2
    println("Number of tenants in apartment: ${apartment.tenants.size}")

    // Show every tenant's details
    apartment.showTenants()

    // THINK ABOUT IT (Task 6):
    // What does it mean when we say an Apartment "has" Tenant objects?
    // ->  The Apartment class holds tenant objects inside it (in its tenants list),
    //    so one class is built from and works together with another.
    //    An Apartment is not a Tenant; it HAS tenants,
    //    and it can manage them (add them, display them).

    // ===================== TASK 8, 9 & 10: Payments, Balance and Status =====================

    println()
    println("===== Tasks 8-10: Partial payments, balance and status =====")

    // Mary makes a partial payment instead of paying in full.
    // Requirement 2: this is tracked in totalPaid and reflected in balanceDue.
    // Requirement 3: a negative amount here would be rejected instead of applied.
    tenant2.payRent(10000.0)
    println("${tenant2.name} total paid: ${tenant2.totalPaid}")
    println("${tenant2.name} balance due: ${tenant2.balanceDue}")
    println("${tenant2.name} status: ${tenant2.status}")

    // Try an invalid (negative) payment — it should be rejected, not applied
    println("Trying to pay -500 for ${tenant2.name}...")
    tenant2.payRent(-500.0)              // Prints "Rent amount cannot be negative."
    println("${tenant2.name} total paid is still: ${tenant2.totalPaid}")

    // Peter (tenant3) never makes a payment, so flag him as overdue manually
    tenant3.markOverdue()

    // Requirement 1: create one more tenant using the DEFAULT rent,
    // by simply not passing a rent argument.
    val tenant4 = Tenant("Grace", 104)
    println("${tenant4.name}'s default rent: ${tenant4.rentAmount}")

    // THINK ABOUT IT (Task 8):
    // Why track totalPaid separately instead of just flipping isPaid to true/false?
    // -> A boolean can only say "paid" or "not paid", but rent is often paid in
    //    installments. Tracking the running total lets us calculate exactly how
    //    much is still owed (balanceDue), and derive isPaid/status from that,
    //    rather than the other way around.

    // THINK ABOUT IT (Task 9):
    // Why reject a negative payment instead of just adding it to totalPaid?
    // -> A negative "payment" would actually increase the balance owed, which
    //    doesn't make sense as a rent payment. Validating it in payRent() stops
    //    bad data from quietly corrupting totalPaid and balanceDue.

    // THINK ABOUT IT (Task 10):
    // Why use an enum (TenantStatus) instead of just the isPaid boolean?
    // -> isPaid can only represent two states. An enum lets us represent a third,
    //    meaningful state (OVERDUE) that isn't just "paid" or "not paid yet" —
    //    it's specifically "behind on payment", which the UI/report can act on
    //    differently.

    // ===================== TASK 11: Formatted Tenant Report =====================

    println()
    println("===== Task 11: Full tenant report =====")

    for (tenant in listOf(tenant1, tenant2, tenant3, tenant4, john, mary)) {
        tenant.printDetails()
    }
}