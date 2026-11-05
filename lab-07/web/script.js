(() => {
    const result = document.getElementById("result-value");
    if (!result) return;

    const query = new URLSearchParams(window.location.search);
    const firstText = query.get("first");
    const secondText = query.get("second");
    const operation = query.get("operation");
    const first = Number(firstText);
    const second = Number(secondText);
    const operations = {
        add: { label: "+", run: (a, b) => a + b },
        subtract: { label: "−", run: (a, b) => a - b },
        multiply: { label: "×", run: (a, b) => a * b },
        divide: { label: "÷", run: (a, b) => a / b }
    };

    const set = (id, value) => { document.getElementById(id).textContent = value; };
    const format = value => Number.isFinite(value) ? new Intl.NumberFormat("ru-RU", { maximumFractionDigits: 12 }).format(value) : "Ошибка";
    const valid = firstText !== null && secondText !== null && firstText.trim() !== "" && secondText.trim() !== ""
        && Number.isFinite(first) && Number.isFinite(second) && Object.hasOwn(operations, operation);

    if (!valid) {
        set("status", "Некорректные аргументы или операция");
        set("result-value", "Ошибка ввода");
        return;
    }

    set("first-value", format(first));
    set("second-value", format(second));
    set("operation-value", operations[operation].label);
    if (operation === "divide" && second === 0) {
        set("status", "Нельзя делить на ноль");
        set("result-value", "Деление на ноль");
        return;
    }
    set("result-value", format(operations[operation].run(first, second)));
})();
