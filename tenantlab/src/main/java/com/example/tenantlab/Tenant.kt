package com.example.tenantlab

// Requirement 4: a tenant's payment standing. ACTIVE = owes something but is
// in good standing, PAID = balance fully settled, OVERDUE = flagged behind.
enum class TenantStatus {
    ACTIVE,
    PAID,
    OVERDUE
}

// The text inside the brackets after the class name is our primary constructor.
// It receives the information we must provide when creating a Tenant.
// - "val name" and "val apartmentNumber" are properties that cannot be changed.
// - "initialRent" is only a plain parameter (no val/var). We pass it through the
//   rentAmount setter below, so the rent gets validated even at creation time.
//
// Requirement 1: initialRent now has a default value, so you can write
// Tenant("Peter", 103) without supplying a rent and still get a sensible amount.
class Tenant(
    val name: String,            // The tenant's name (cannot change after creation)
    val apartmentNumber: Int,    // The apartment the tenant lives in
    initialRent: Double = DEFAULT_RENT   // The starting rent, checked before it is stored
) {

    companion object {
        const val DEFAULT_RENT = 15000.0
    }

    // A property that tracks whether the rent is fully paid.
    // It always starts as false because a new tenant has not paid yet.
    // It's now derived automatically from payments, so only this class sets it.
    var isPaid: Boolean = false
        private set

    // The rent amount property with a custom getter and a custom setter.
    // "field" is the hidden property that holds the actual value.
    // We start it at 0.0; the init block below sets the real starting value.
    var rentAmount: Double = 0.0

        // CUSTOM GETTER (Task 4): runs every time we read tenant.rentAmount
        get() {
            println("Rent amount accessed.")   // Print a message on every read
            return field                        // Then return the actual stored value
        }

        // CUSTOM SETTER (Task 3): runs every time we write tenant.rentAmount = ...
        // "value" is the new amount someone is trying to store.
        set(value) {
            if (value >= 0) {
                field = value                   // Valid (0 or more): store it
            } else {
                println("Rent amount cannot be negative.")
            }
        }

    // Requirement 2: running total of everything this tenant has paid so far.
    var totalPaid: Double = 0.0
        private set

    // Requirement 2: remaining balance, calculated on the fly rather than stored,
    // so it's always in sync with rentAmount and totalPaid.
    val balanceDue: Double
        get() = (rentAmount - totalPaid).coerceAtLeast(0.0)

    // Requirement 4: current status, kept in sync by refreshStatus() below.
    var status: TenantStatus = TenantStatus.ACTIVE
        private set

    // The init block runs once, right when the object is created.
    // "this.rentAmount" is the property; "initialRent" is the constructor parameter.
    // Assigning here triggers the setter, so a negative starting rent is also rejected.
    init {
        this.rentAmount = initialRent
    }

    /**
     * A function that records a rent payment.
     *
     * Requirement 3: validation — a negative amount is rejected and nothing
     * is recorded, instead of silently corrupting totalPaid.
     * Requirement 2: adds to totalPaid and refreshes balanceDue/status.
     */
    fun payRent(amount: Double = this.balanceDue) {
        if (amount < 0) {
            println("Rent amount cannot be negative.")   // same style of message as the setter
            return
        }
        totalPaid += amount
        refreshStatus()
        println("Rent paid successfully by $name")      // $name inserts the tenant's name
    }

    /** Lets you flag a tenant overdue manually (e.g. they missed a due date). */
    fun markOverdue() {
        if (status != TenantStatus.PAID) {
            status = TenantStatus.OVERDUE
        }
    }

    private fun refreshStatus() {
        status = if (balanceDue <= 0.0) TenantStatus.PAID else TenantStatus.ACTIVE
        isPaid = (status == TenantStatus.PAID)
    }

    /** Requirement 5: a clear, formatted summary of this tenant's details. */
    fun printDetails() {
        println("----------------------------------------")
        println("Name: $name")
        println("Apartment: $apartmentNumber")
        println("Monthly Rent: Ksh ${"%.2f".format(rentAmount)}")
        println("Total Paid: Ksh ${"%.2f".format(totalPaid)}")
        println("Balance Due: Ksh ${"%.2f".format(balanceDue)}")
        println("Status: $status")
        println("----------------------------------------")
    }
}

/*
 ===================== TASK 1 VERSION (kept for reference) =====================
 This is the first version of the Tenant class, before we added a constructor.
 Properties are given default values, then filled in after the object is created.

 class Tenant {
     var name: String = ""              // Tenant's name (empty until we set it)
     var apartmentNumber: Int = 0       // Apartment number (0 until we set it)
     var rentAmount: Double = 0.0       // Monthly rent (0.0 until we set it)
     var isPaid: Boolean = false        // Starts as false: rent not yet paid

     // Marks the rent as paid and prints a confirmation message
     fun payRent() {
         isPaid = true
         println("Rent paid successfully by $name")
     }
 }

 How it was used in main() for Task 1:

 val tenant1 = Tenant()          // Create an empty tenant object
 tenant1.name = "John"           // Fill in its details one by one
 tenant1.apartmentNumber = 101
 tenant1.rentAmount = 15000.0

 val tenant2 = Tenant()
 tenant2.name = "Mary"
 tenant2.apartmentNumber = 102
 tenant2.rentAmount = 18000.0

 tenant1.payRent()               // Only tenant1 pays
 println("${tenant1.name} paid? ${tenant1.isPaid}")
 println("${tenant2.name} paid? ${tenant2.isPaid}")

 Task 2 improved on this by passing the details through a primary
 constructor, so an object is complete as soon as it is created.
*/

/**
 * Requirement 6: demo entry point — creates at least 3 tenants and
 * demonstrates payments, balance checks, validation, and status printing.
 */
fun main() {
    val tenant1 = Tenant("John", 101, 15000.0)
    val tenant2 = Tenant("Mary", 102, 18000.0)
    val tenant3 = Tenant("Peter", 103)   // uses the default rent (DEFAULT_RENT)

    tenant1.payRent(15000.0)   // pays in full -> becomes PAID
    tenant2.payRent(10000.0)   // partial payment -> stays ACTIVE with a balance
    tenant3.markOverdue()      // never pays -> flagged OVERDUE

    // Demonstrate validation: this payment is rejected, not applied
    tenant2.payRent(-500.0)

    println("\n===== TENANT REPORT =====\n")
    for (tenant in listOf(tenant1, tenant2, tenant3)) {
        tenant.printDetails()
        println("Remaining balance for ${tenant.name}: Ksh ${"%.2f".format(tenant.balanceDue)}\n")
    }
}