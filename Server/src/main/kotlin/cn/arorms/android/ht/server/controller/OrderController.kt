package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.entity.Order
import cn.arorms.android.ht.server.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController @Autowired constructor(
    private val orderService: OrderService
) {

    // Get all orders
    @GetMapping
    fun getAllOrders(): ResponseEntity<List<Order>> {
        val orders = orderService.getAllOrders()
        return ResponseEntity(orders, HttpStatus.OK)
    }

    // Get order by ID
    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): ResponseEntity<Order> {
        val order = orderService.getOrderById(id)
        return if (order.isPresent) {
            ResponseEntity(order.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Get orders by user ID
    @GetMapping("/user/{userId}")
    fun getOrdersByUserId(@PathVariable userId: Long): ResponseEntity<List<Order>> {
        val orders = orderService.getOrdersByUserId(userId)
        return ResponseEntity(orders, HttpStatus.OK)
    }

    // Get orders by state
    @GetMapping("/state/{state}")
    fun getOrdersByState(@PathVariable state: String): ResponseEntity<List<Order>> {
        val orders = orderService.getOrdersByState(state)
        return ResponseEntity(orders, HttpStatus.OK)
    }

    // Create new order
    @PostMapping
    fun createOrder(@RequestBody order: Order): ResponseEntity<Order> {
        val createdOrder = orderService.createOrder(order)
        return ResponseEntity(createdOrder, HttpStatus.CREATED)
    }

    // Update order
    @PutMapping("/{id}")
    fun updateOrder(@PathVariable id: Long, @RequestBody orderDetails: Order): ResponseEntity<Order> {
        try {
            val updatedOrder = orderService.updateOrder(id, orderDetails)
            return ResponseEntity(updatedOrder, HttpStatus.OK)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Delete order
    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Void> {
        try {
            orderService.deleteOrder(id)
            return ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
