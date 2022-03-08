# react-native-view-visibility

react-native-view-visibility

## Installation

```sh
npm install react-native-view-visibility
```

## Usage

```js
import { ViewVisibility } from "react-native-view-visibility";

// ...

<ViewVisibility idBanner={'12345'}>
  // ...
</ViewVisibility>
```

**AppNexusBanner props:**

| Name | Description                                                                                                                                                                                                                                | Required | Type   | Example                                                                   |
| --- |--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:--------:|--------|---------------------------------------------------------------------------|
| `idBanner` | The placement ID identifies your banner in the system.                                                                                                                                                                                     | **YES**  | String | `"1234567"`                                                               |
| `percentVisibility` | The offset when the banner is considered visible (percentage of the entire banner view holder). Default: `50`                                                                                                                              |    No    | Number | 50                                                                        |
| `horizontal` | If used in horizontal scrolling, you need to install true                                                                                                                                                                                  |    No     | Boolean | false                                                                     |
| `onAdVisibleChange` | A callback triggered when the visibility of the banner has been changed. Returns a visibility type (`0` - if banner is not visible, `1` - if banner is partially visible, `2` - If banner is percent visible,   `3` - If banner is fully visible | No | Function | `(visibilityType: number) => console.log("The visibility has changed", visibilityType)` |

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
