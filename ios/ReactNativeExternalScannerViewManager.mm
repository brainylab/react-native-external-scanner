#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>
#import "RCTBridge.h"

@interface ReactNativeExternalScannerViewManager : RCTViewManager
@end

@implementation ReactNativeExternalScannerViewManager

RCT_EXPORT_MODULE(ReactNativeExternalScannerView)

- (UIView *)view
{
  return [[UIView alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(color, NSString)

@end
