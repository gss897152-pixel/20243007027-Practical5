# Verification results

Checked on 30 October 2026 using a Medium Phone emulator running Android 15 / API 35, at 1080 x 2400 pixels and 420 dpi. Application logic is Java; both screens use XML layouts.

## Build and automated tests

- Debug APK built successfully.
- Release APK built successfully, including R8 optimisation. The generated release APK is unsigned; the debug APK is used for emulator testing.
- `TextProcessorTest`: **6 tests passed**, 0 failures, 0 errors, 0 skipped.
- `MunicipalityFlowTest`: **7 tests passed**, 0 failures, 0 errors, 0 skipped.
- `lintDebug`: **0 errors**. Remaining advisory warnings are seven dependency update suggestions and five image density-location warnings. The images remain in `res/drawable/` as specifically required by the practical. No lint checks were disabled.

Device tests cover all nine required inputs, normalised intent extras, all four image/summary mappings, 80-120 word summary lengths, named colours, text contrast, four implicit website intents, safe handling of missing/invalid intent data, and recreation of the details Activity.

| Input | Verified result |
| --- | --- |
| Beijing | Beijing details |
| beijing | Beijing details |
| BEIJING | Beijing details |
| Shanghai | Shanghai details |
| Tianjin | Tianjin details |
| Chongqing | Chongqing details |
| Guangzhou | Input error; main screen remains open |
| Empty | Input error; main screen remains open |
| Spaces only | Input error; main screen remains open |

Unit tests also cover `null`, surrounding whitespace, mixed case, partial/combined names, and a Turkish default locale. The details screen independently reprocesses the received name with the same `TextProcessor`.

## Runtime checks

All four screens were opened through the main input field. Physical Enter and the software keyboard Go action were checked. Each screen displays the matching course-supplied image and summary, the required city colour on its title background, information text and button, and contrasting title/button text. City title, image and button stay outside the information ScrollView.

Night mode, landscape orientation and landscape with 1.6x system text size were checked. The information remains scrollable and Visit site stays accessible. Display settings were restored after testing. Chongqing uses dark text on its orange title/button and orange information text on a dark neutral panel to keep the required orange readable.

| City | Array index | Summary words | Named colour |
| --- | ---: | ---: | --- |
| Beijing | 0 | 100 | Red, `#D32F2F` |
| Shanghai | 1 | 99 | Magenta, `#B000B5` |
| Chongqing | 2 | 101 | Bright orange, `#FF8F00` |
| Tianjin | 3 | 101 | Charcoal, `#36454F` |

The five application image files were also compared with the course resources by SHA-256 and are unchanged. Adaptive icons and legacy icons are present, including all five generated density sizes. The generated icon name remains `ic_launcher`.

## Official websites

Each Visit site button was clicked in the running app and opened an external application (`org.chromium.webview_shell`, WebView Browser Tester). The browser's actual URL and page title were inspected. All four official homepages also returned HTTP 200 when checked independently.

| City | Opened URL | Loaded page title |
| --- | --- | --- |
| Beijing | https://english.beijing.gov.cn/ | BEIJING CHINA-International Web Portal of Beijing |
| Shanghai | https://english.shanghai.gov.cn/ | SHANGHAI CHINA-International Services Shanghai |
| Chongqing | https://english.cq.gov.cn/ | CHONGQING · CHINA |
| Tianjin | https://en.tj.gov.cn/ | Tianjin Municipal People's Government |

For the no-handler check, the emulator's external browser was temporarily disabled and Android reported that no Activity could handle the HTTPS URL. Clicking Visit site displayed “No application is available to open this website.” The municipality screen remained open without a crash. The browser was then re-enabled and its HTTPS handler was verified again.

## Screenshots

| Screen | Evidence |
| --- | --- |
| Main | [Main screen](screenshots/main.png) |
| Beijing | [Red colour scheme](screenshots/beijing.png) |
| Shanghai | [Magenta colour scheme](screenshots/shanghai.png) |
| Chongqing | [Orange colour scheme](screenshots/chongqing.png) |
| Tianjin | [Charcoal colour scheme](screenshots/tianjin.png) |
| Night mode | [Readable system icons](screenshots/night.png) |
| Landscape, large text, scrolled | [Visit site remains visible](screenshots/landscape-large-text.png) |
| No external browser | [Fallback message](screenshots/no-browser.png) |
| Official sites | [Beijing](screenshots/beijing-site.png), [Shanghai](screenshots/shanghai-site.png), [Chongqing](screenshots/chongqing-site.png), [Tianjin](screenshots/tianjin-site.png) |

## Submission check

Repository: [20243007027-Practical5](https://github.com/gss897152-pixel/20243007027-Practical5). It was created blank and public, without GitHub-generated initial files. The initial generated project was added, committed and pushed using Android Studio's built-in Git tools. Later commits record resources, the main layout, validation, explicit navigation, styling, website intents, and verification evidence. Local settings, build outputs and `local.properties` are excluded.

The remaining submission step is to paste this public repository URL into the Practical 5 submission on Canvas. A GitHub push does not submit the assignment to Canvas.
