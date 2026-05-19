#import "shapesnap_ads_bridge.h"
#import <GoogleMobileAds/GoogleMobileAds.h>
#import <UIKit/UIKit.h>

void shapesnap_ads_start_if_needed(void) {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        [GADMobileAds.sharedInstance startWithCompletionHandler:nil];
    });
}

static UIViewController *_Nullable shapesnap_ads_key_root_view_controller(void) {
    NSSet<UIScene *> *scenes = UIApplication.sharedApplication.connectedScenes;
    for (UIScene *scene in scenes) {
        if (![scene isKindOfClass:[UIWindowScene class]]) {
            continue;
        }
        UIWindowScene *windowScene = (UIWindowScene *)scene;
        for (UIWindow *window in windowScene.windows) {
            if (window.isKeyWindow) {
                return window.rootViewController;
            }
        }
        return windowScene.windows.firstObject.rootViewController;
    }
    return nil;
}

UIView *shapesnap_ads_create_test_banner(void) {
    shapesnap_ads_start_if_needed();
    GADBannerView *banner = [[GADBannerView alloc] initWithAdSize:GADAdSizeBanner];
    banner.adUnitID = @"ca-app-pub-3940256099942544/2934735716";
    banner.rootViewController = shapesnap_ads_key_root_view_controller();
    [banner loadRequest:[GADRequest request]];
    return banner;
}
