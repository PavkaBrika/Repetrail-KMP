# Официальные ориентиры

Проверены 6 ноября 2026. Читай при обсуждении визуального направления или перед изменением платформенной интеграции; актуальные API сверяй с версией проекта.

| Источник | Что применяем в Repetrail |
| --- | --- |
| [Apple HIG — Materials](https://developer.apple.com/design/human-interface-guidelines/materials) | Близкий ориентир: управление и навигация образуют отдельный плавающий слой над контентом. Материалы применяются избирательно с приоритетом читаемости. |
| [Apple WWDC25 — Meet Liquid Glass](https://developer.apple.com/videos/play/wwdc2025/219/) | Визуальный референс поведения плавающего управления. Bubble-формы Repetrail не требуют воспроизведения оптики Liquid Glass. |
| [Android — Edge-to-edge design](https://developer.android.com/design/ui/mobile/guides/layout-and-content/edge-to-edge) | Фон и прокручиваемый контент продолжаются за системными барами; интерактивные элементы учитывают системные области и вырезы. |
| [Compose — Set up Edge-to-edge](https://developer.android.com/develop/ui/compose/system/setup-e2e) | Настройка окна, IME и системных иконок на Android-границе приложения. |
| [Compose — Set up window insets](https://developer.android.com/develop/ui/compose/system/insets-ui) | Разница между padding viewport и обработкой insets у controls, consumption и совместная работа с IME. |
| [Compose — About system bar protection](https://developer.android.com/develop/ui/compose/system/system-bars) | Прозрачность gesture navigation и отдельное поведение contrast scrim в three-button navigation. |

Эти материалы подтверждают отдельные принципы; они не задают готовый Repetrail-скин. Круглая кнопка назад, заголовок с опциональным переходом в центральную верхнюю bubble, меню в верхнем end-углу и FAB над нижней навигацией — выбранные пользователем правила Repetrail. Цвета, типографика, формы и spacing определяются существующими токенами приложения.
