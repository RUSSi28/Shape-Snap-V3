#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

/** Kotlin cinterop 用ヘッダーと同一 API（composeApp/src/nativeInterop/cinterop/shapesnap_ads_bridge.h と同期すること） */
UIView *shapesnap_ads_create_test_banner(void);
void shapesnap_ads_start_if_needed(void);

NS_ASSUME_NONNULL_END
