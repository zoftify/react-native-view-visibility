#import "ViewVisibilityView.h"

typedef NS_ENUM(NSInteger)
{
    BANNER_NOT_VISIBLE                 = 0,
    BANNER_PARTIALLY_VISIBLE           = 1,
    BANNER_PERCENT_VISIBLE             = 2,
    BANNER_FULLY_VISIBLE               = 3,
};

@implementation ViewVisibilityView {
    BOOL _isLoaded;
    NSInteger _isVisible;
    BOOL _isSendNotification;
}

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher {
    if (self = [super init]) {
        _isLoaded = NO;
        _isVisible = BANNER_NOT_VISIBLE;
        _isSendNotification = NO;
    }
    return self;
}

- (void)layoutSubviews {
    super.removeClippedSubviews = YES;
    [super layoutSubviews];
}

- (void)react_updateClippedSubviewsWithClipRect:(CGRect)clipRect relativeToView:(UIView *)clipView
{
    // Convert clipping rect to local coordinates
    clipRect = [clipView convertRect:clipRect toView:self];
    clipRect = CGRectIntersection(clipRect, self.bounds);
    clipView = self;

    if (!CGSizeEqualToSize(CGRectIntersection(clipRect, self.frame).size, CGSizeZero)) {
        if (!CGRectContainsRect(clipRect, self.frame)) {
            if (_horizontal) {
                [self calculationVisibilityHorizontal:clipRect];
            } else {
                [self calculationVisibilityVertical:clipRect];
            }
        } else {
            if (_isVisible != BANNER_FULLY_VISIBLE) {
                _isVisible = BANNER_FULLY_VISIBLE;

                if (_isSendNotification == NO) {
                    _isSendNotification = YES;
                    _onAdVisibleChangeReceived(@{@"visible": @(BANNER_PERCENT_VISIBLE), @"id": _idBanner});
                }

                _onAdVisibleChangeReceived(@{@"visible": @(BANNER_FULLY_VISIBLE), @"id": _idBanner});
            }
        }
    } else {
        if (_isVisible != BANNER_NOT_VISIBLE) {
            _isSendNotification = NO;
            _isVisible = BANNER_NOT_VISIBLE;
            _onAdVisibleChangeReceived(@{@"visible": @(BANNER_NOT_VISIBLE), @"id": _idBanner});
        }
    }
}

- (void) calculationVisibilityHorizontal:(CGRect)clipRect {
    int percents = 0;

    if (self.frame.size.width == clipRect.size.width) {
        if (_isVisible != BANNER_FULLY_VISIBLE) {
            _isVisible = BANNER_FULLY_VISIBLE;

            if (_isSendNotification == NO) {
                _isSendNotification = YES;
                _onAdVisibleChangeReceived(@{@"visible": @(BANNER_PERCENT_VISIBLE), @"id": _idBanner});
            }

            _onAdVisibleChangeReceived(@{@"visible": @(BANNER_FULLY_VISIBLE), @"id": _idBanner});
        }

        return;
    }

    if (clipRect.origin.x > 0 && clipRect.size.width < self.frame.size.width) {
        percents = (self.frame.size.width - clipRect.origin.x) * 100 / self.frame.size.width;
        if (percents > 0) {
            [self onPercentVisibility:percents];
        }
    } else if (clipRect.origin.x == 0 && clipRect.size.width > 0) {
        percents = 100 - ((self.frame.size.width - clipRect.size.width) * 100 / self.frame.size.width);
        if (percents > 0) {
            [self onPercentVisibility:percents];
        }
    }
}

- (void) calculationVisibilityVertical:(CGRect)clipRect {
    int percents = 0;

    if (self.frame.size.height == clipRect.size.height) {
        if (_isVisible != BANNER_FULLY_VISIBLE) {
            _isVisible = BANNER_FULLY_VISIBLE;

            if (_isSendNotification == NO) {
                _isSendNotification = YES;
                _onAdVisibleChangeReceived(@{@"visible": @(BANNER_PERCENT_VISIBLE), @"id": _idBanner});
            }

            _onAdVisibleChangeReceived(@{@"visible": @(BANNER_FULLY_VISIBLE), @"id": _idBanner});
        }

        return;
    }

    if (clipRect.origin.y > 0 && clipRect.size.height < self.frame.size.height) {
        percents = (self.frame.size.height - clipRect.origin.y) * 100 / self.frame.size.height;
        if (percents > 0) {
            [self onPercentVisibility:percents];
        }
    } else if (clipRect.origin.y == 0 && clipRect.size.height > 0) {
        percents = 100 - ((self.frame.size.height - clipRect.size.height) * 100 / self.frame.size.height);
        if (percents > 0) {
            [self onPercentVisibility:percents];
        }
    }
}

- (void) onPercentVisibility:(NSInteger)percents {
    if (_isVisible != BANNER_PARTIALLY_VISIBLE) {
        _isVisible = BANNER_PARTIALLY_VISIBLE;
        _onAdVisibleChangeReceived(@{@"visible": @(BANNER_PARTIALLY_VISIBLE), @"id": _idBanner});
    }

    if (percents > (NSInteger)_percentVisibility && _isSendNotification == NO && _isVisible == BANNER_PARTIALLY_VISIBLE) {
        _isSendNotification = YES;
        _onAdVisibleChangeReceived(@{@"visible": @(BANNER_PERCENT_VISIBLE), @"id": _idBanner});
    }
}

@end
