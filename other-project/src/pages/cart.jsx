import { useEffect, useState } from "react";
import {
  getCart,
  removeFromCart,
  clearCart,
  addToCart,
  decreaseFromCart,
} from "../utils/cart";
import { formatPrice } from "../utils/formatPrice";

function getImageUrl(base, path, placeholder) {
  if (!path)
    return placeholder ? getImageUrl(base, placeholder) : "/placeholder.png";
  const cleanBase = base.replace(/\/+$/, "");
  const cleanPath = path.replace(/^\/+/, "");
  return `${cleanBase}/${cleanPath}`;
}

export default function CartPage({ isDark }) {
  const [cart, setCart] = useState([]);
  const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

  useEffect(() => {
    const updateCart = () => setCart(getCart());
    updateCart();
    window.addEventListener("cart-updated", updateCart);
    return () => window.removeEventListener("cart-updated", updateCart);
  }, []);

  useEffect(() => {
    // Если пользователь разлогинился, сбрасываем все данные пользователя
    const onStorage = (e) => {
      if (
        e.key === "username" ||
        e.key === "userId" ||
        e.key === "email" ||
        e.key === "isAdmin"
      ) {
        // Можно добавить сброс состояния, если нужно
        // Например, window.location.reload();
      }
    };
    window.addEventListener("storage", onStorage);
    return () => window.removeEventListener("storage", onStorage);
  }, []);

  const handleRemove = (id) => {
    removeFromCart(id);
    setCart(getCart());
  };

  const handleClear = () => {
    clearCart();
    setCart([]);
  };

  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

  return (
    <div
      className={`cart-page${isDark ? " dark-mode" : ""}`}
      style={{
        maxWidth: 700,
        margin: "40px auto",
        background: isDark ? "var(--card-bg-dark)" : "#fff",
        color: isDark ? "var(--text-light)" : undefined,
        borderRadius: 16,
        boxShadow: "0 4px 24px #0001",
        padding: 32,
      }}
    >
      <h2
        style={{
          textAlign: "center",
          marginBottom: 32,
          fontSize: 32,
          letterSpacing: 1,
        }}
      >
        🛒 Ваша корзина
      </h2>
      {cart.length === 0 ? (
        <p style={{ textAlign: "center", color: "#888", fontSize: 20 }}>
          Корзина пуста.
        </p>
      ) : (
        <>
          <ul style={{ listStyle: "none", padding: 0, margin: 0 }}>
            {cart.map((item) => {
              const imageUrl = getImageUrl(
                API_BASE_URL,
                item.imageUrl,
                item.placeholderImageUrl
              );
              return (
                <li
                  key={item.id}
                  style={{
                    marginBottom: 24,
                    borderBottom: "1px solid #eee",
                    paddingBottom: 18,
                    display: "flex",
                    alignItems: "center",
                    gap: 24,
                    background: isDark ? "var(--background-secondary, #232323)" : "#fafbfc",
                    borderRadius: 12,
                    boxShadow: "0 2px 8px #0001",
                    padding: 16,
                  }}
                >
                  <img
                    src={imageUrl}
                    alt={item.name}
                    style={{
                      maxWidth: 90,
                      maxHeight: 90,
                      borderRadius: 10,
                      objectFit: "cover",
                      background: "#fff",
                      boxShadow: "0 1px 4px #0001",
                      marginRight: 8,
                    }}
                  />
                  <div style={{ flex: 1 }}>
                    <div style={{ fontWeight: 600, fontSize: 20 }}>
                      {item.name}
                    </div>
                    <div
                      style={{ color: "#666", fontSize: 15, margin: "6px 0" }}
                    >
                      {item.category?.name}
                    </div>
                    <div style={{ color: "#888", fontSize: 14 }}>
                      {item.description}
                    </div>
                  </div>
                  <div style={{ minWidth: 120, textAlign: "right" }}>
                    <div style={{ fontWeight: 500, fontSize: 18 }}>
                      {formatPrice(item.price)} ₽
                    </div>
                  </div>
                  <div
                    style={{
                      width: "100%",
                      display: "flex",
                      flexDirection: "column",
                      alignItems: "flex-end",
                      marginTop: 8,
                      gap: 8,
                    }}
                  >
                    <div
                      style={{ display: "flex", alignItems: "center", gap: 8 }}
                    >
                      <button
                        className="btn"
                        style={{
                          minWidth: 32,
                          height: 32,
                          fontSize: 18,
                          padding: 0,
                          background: "#d32f2f",
                          color: "#fff",
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "center",
                        }}
                        onClick={() => decreaseFromCart(item.id)}
                        disabled={item.quantity === 1}
                      >
                        -
                      </button>
                      <span
                        style={{
                          minWidth: 28,
                          textAlign: "center",
                          fontWeight: 600,
                          fontSize: 18,
                        }}
                      >
                        {item.quantity}
                      </span>
                      <button
                        className="btn"
                        style={{
                          minWidth: 32,
                          height: 32,
                          fontSize: 18,
                          padding: 0,
                          background: "#d32f2f",
                          color: "#fff",
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "center",
                        }}
                        onClick={() => addToCart(item)}
                        disabled={item.quantity >= item.stockQuantity}
                      >
                        +
                      </button>
                    </div>
                    <button
                      className="btn"
                      style={{
                        width: 120,
                        background: "#d32f2f",
                        color: "#fff",
                        fontWeight: 600,
                      }}
                      onClick={() => handleRemove(item.id)}
                    >
                      Удалить
                    </button>
                  </div>
                </li>
              );
            })}
          </ul>
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              marginTop: 32,
              fontWeight: "bold",
              fontSize: 22,
            }}
          >
            <span>Итого:</span>
            <span style={{ color: "#27ae60" }}>{formatPrice(total)} ₽</span>
          </div>
          <button
            className="btn"
            style={{
              marginTop: 32,
              width: "100%",
              background: "#222",
              color: "#fff",
              fontSize: 18,
              padding: "14px 0",
              borderRadius: 8,
            }}
            onClick={handleClear}
          >
            Очистить корзину
          </button>
          <button
            className="btn"
            style={{
              marginTop: 16,
              width: "100%",
              background: "#d32f2f",
              color: "#fff",
              fontSize: 18,
              padding: "14px 0",
              borderRadius: 8,
              fontWeight: 600,
            }}
            onClick={async () => {
              const userId = Number(localStorage.getItem("userId"));
              if (!userId) {
                alert("Необходимо войти в аккаунт для оформления заказа");
                return;
              }
              const orderItems = cart.map((item) => ({
                productId: item.id ?? item.productId,
                quantity: item.quantity,
              }));
              try {
                const res = await fetch("/api/orders/create-order", {
                  method: "POST",
                  headers: { "Content-Type": "application/json" },
                  body: JSON.stringify({ userId, orderItems }),
                });
                console.log("Создаём заказ, отправляем:", { userId, orderItems });
                if (!res.ok) throw new Error("Ошибка оформления заказа");
                alert("Заказ успешно оформлен!");
                handleClear();
              } catch (e) {
                alert(e.message);
              }
            }}
          >
            Оформить заказ
          </button>
        </>
      )}
    </div>
  );
}
