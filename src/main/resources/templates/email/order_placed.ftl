<h1>Hello, ${customer.name}</h1>
<h2>Your order ${id} has been placed!</h2>

<table border="1">
    <thead>
        <tr>
            <th>Item</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Subtotal</th>
        </tr>
    </thead>

    <tbody>
        <#list items as item>
            <tr>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td>${item.qty}</td>
                <td align="right">${item.subtotal}</td>
            </tr>
        </#list>
    </tbody>

    <tfoot>
        <tr>
            <td colspan="3" align="right">Discount</td>
            <td align="right">${discount}</td>
        </tr>
        <tr>
            <td colspan="3" align="right">Total</td>
            <td align="right">${total}</td>
        </tr>
    </tfoot>
</table>

<#if status != 'Paid'>
    <p>Your order will be delivered as soon as the payment processing ends.</p>
</#if>

<p>Thank for your order! Enjoy your meal!</p>
