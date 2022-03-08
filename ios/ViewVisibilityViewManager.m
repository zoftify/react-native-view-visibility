#import <React/RCTViewManager.h>
#import "ViewVisibilityView.h"

@interface ViewVisibilityViewManager : RCTViewManager
@end

@implementation ViewVisibilityViewManager

RCT_EXPORT_MODULE(ViewVisibilityView)

- (UIView *)view
{
  return [[ViewVisibilityView alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
}

- (instancetype)init
{
    self = [super init];
    return self;
}

+ (BOOL)requiresMainQueueSetup
{
    return NO;
}

RCT_EXPORT_VIEW_PROPERTY(percentVisibility, NSInteger *)
RCT_EXPORT_VIEW_PROPERTY(idBanner, NSString *)
RCT_EXPORT_VIEW_PROPERTY(horizontal, BOOL *)
RCT_EXPORT_VIEW_PROPERTY(onAdVisibleChangeReceived, RCTBubblingEventBlock)

@end
