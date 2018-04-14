import java.time.LocalDateTime

sealed class Component(val name: String, val price: Int, val maxStock: Int = 10) {
    var stock: Int = maxStock
    class Processor(name: String, price: Int) : Component(name, price)
    class RAM(name: String, price: Int) : Component(name, price)
    class Storage(name: String, price: Int) : Component(name, price)
    class Screen(name: String, price: Int) : Component(name, price)
    class Case(name: String, price: Int) : Component(name, price)
    class Port(name: String, price: Int) : Component(name, price)
}

data class Order(val details: String?, val components: ArrayList<Component>, val orderDate: LocalDateTime = LocalDateTime.now())

fun main(vararg args: String) {
    // Array of arrays of components (all available components)
    val optionOptions: Array<Array<out Component>> = arrayOf(
            arrayOf(Component.Processor("p3", 100),
                    Component.Processor("p5", 120),
                    Component.Processor("p7", 200)),
            arrayOf(Component.RAM("16 GB", 75),
                    Component.RAM("32 GB", 150)),
            arrayOf(Component.Storage("1 TB", 50),
                    Component.Storage("2 TB", 100)),
            arrayOf(Component.Screen("19\"", 65),
                    Component.Screen("23\"", 120)),
            arrayOf(Component.Case("Mini", 40),
                    Component.Case("Midi", 70)),
            arrayOf(Component.Port("2 ports", 10),
                    Component.Port("4 ports", 20))
    )

    var endOfDay = false


    val orders: ArrayList<Order> = ArrayList() // An arraylist that will store all orders

    while (!endOfDay) { // Main loop for continuing whilst not end of day
        val chosenComponents: ArrayList<Component> = ArrayList()
        for (options in optionOptions) {
            while (true) {
                println("Available components")
                options.forEachIndexed { index, component ->
                    println("$index: ${component.name} @ £${component.price}   (${component.stock}/10)") // Print available options and their selection numbers
                }
                print("Please select an option: ")
                val choice = readLine()?.toIntOrNull()

                if (choice != null) {
                    val componentChoice: Component? = options.getOrNull(choice)

                    if (componentChoice != null) {
                        if (componentChoice.stock > 0) { // Make sure there is stock available for the component
                            println("Selected ${componentChoice.name}")
                            chosenComponents.add(componentChoice) // Add the valid component to a list of all chosen components

                            break // A valid selection has been made, so leave the loop ensuring that a valid selection is made
                        } else {
                            println("There is no stock for that component")
                            println("Please select one with stock available")
                        }
                    }
                }

                println("Invalid selection, please try again")
            }


        }

        println("Order Summary")
        for (component in chosenComponents) {
            println(component.name.padEnd(10) + "| +£${component.price}")
        }
        val componentSum = chosenComponents.sumBy {it.price} // Calculate the sum of all components in the order
        val tax = componentSum * 0.2 // Calculate tax (20%)
        println("Tax       | +£$tax") // Print tax
        println()
        val totalSum = componentSum + tax // Calculate the total price
        println("Total     | £$totalSum")

        while (true) {
            println("Would you like to confirm this order?")
            print("Please choose an option (y/n) : ")
            val input = readLine()
            if (input != null) {
                if (input.toLowerCase() == "y") {
                    print("Please enter customer's details: ")
                    val details: String? = readLine()

                    for (component in chosenComponents) {
                        --component.stock
                    }

                    orders.add(Order(details, chosenComponents))
                } else {
                    println("Order abandoned")
                }
                break // Break from loop regardless of input (provided there is input)
            }
        }

        print("Is it the end of the day? (y/n) : ")
        if (readLine()?.toLowerCase() ?: "" == "y") {endOfDay = true} // Checks if it is the end of the day and if it is breaks loop
    }

    // Start of end of day summary
    println("End of day summary: ")
    println()
    println("Order summary: ")
    println()
    println("${orders.size} orders made in total")
    println()
    orders.forEachIndexed { index, order ->  println("$index: £${order.components.sumBy { it.price }} ordered by ${order.details}")} // Prints the id of every over and the price of it
    println()
    println("Components sold summary: ")
    println()

    // Loops every available component
    // Filters all ordered component on if they have the same name as the looped component
    // Sums up the total bought (maxStock - stock) for those filtered components
    optionOptions.forEach {options -> options.forEach { component: Component ->  orders.forEach { order -> println("${component.name}: ${order.components.filter { orderComponent -> orderComponent.name == component.name}.sumBy { it.maxStock - it.stock }}")} }}
}