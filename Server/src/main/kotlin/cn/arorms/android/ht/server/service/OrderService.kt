package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.models.Order
import cn.arorms.android.ht.server.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService @Autowired constructor(
    private val orderRepository: OrderRepository
) {

    // Get all orders
    fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }

    // Get order by ID
    fun getOrderById(id: Long): Optional<Order> {
        return orderRepository.findById(id)
    }

    // Get orders by user ID
    fun getOrdersByUserId(userId: Long): List<Order> {
        return orderRepository.findByUserId(userId)
    }

    // Get orders by state
    fun getOrdersByState(state: String): List<Order> {
        return orderRepository.findByState(state)
    }

    // Create new order
    fun createOrder(order: Order): Order {
        return orderRepository.save(order)
    }

    // Update order
    fun updateOrder(id: Long, orderDetails: Order): Order {
        val order = orderRepository.findById(id)
            .orElseThrow { RuntimeException("Order not found with id: $id") }

        order.user = orderDetails.user
        order.bookname = orderDetails.bookname
        order.count = orderDetails.count
        order.price = orderDetails.price
        order.state = orderDetails.state

        return orderRepository.save(order)
    }

    // Delete order
    fun deleteOrder(id: Long) {
        val order = orderRepository.findById(id)
            .orElseThrow { RuntimeException("Order not found with id: $id") }
        orderRepository.delete(order)
    }

    // Check if order exists
    fun existsById(id: Long): Boolean {
        return orderRepository.existsById(id)
    }
}
