const app = angular.module('notebook', []);

const CONSTANT = {
    MENU_TYPE_MODULE: 'module',
    MODULE_MENU_PATH: "/menu.json",
    KEY_MENU_SELECTED: "KEY_MENU_SELECTED",
    KEY_ORIGIN_SELECTED: "KEY_ORIGIN_SELECTED"
}

const ORIGINS = [
    {
        NAME: "开发记录",
        ROOT_PATH: "./dev",
        CONFIG_PATH: "/dev_config.json"
    },
    {
        NAME: "java笔记",
        ROOT_PATH: "./note",
        CONFIG_PATH: "/java_note_config.json"
    },
    {
        NAME: "typescript笔记",
        ROOT_PATH: "./note",
        CONFIG_PATH: "/typescript_note_config.json"
    },
    {
        NAME: "raspberrypi笔记",
        ROOT_PATH: "./note",
        CONFIG_PATH: "/raspberrypi_note_config.json"
    },
    {
        NAME: "阿里云记录",
        ROOT_PATH: "https://ch-note.oss-cn-shanghai.aliyuncs.com",
        CONFIG_PATH: "/config.json"
    }
]

let ORIGIN = ORIGINS[0];

function setOrigin (origin) {
    ORIGIN = origin;
}

app.config(['$sceDelegateProvider', function($sceDelegateProvider) {
    $sceDelegateProvider.trustedResourceUrlList([
        'self',
        'https://ch-note.oss-cn-shanghai.aliyuncs.com/**'
    ]);

    //highlight忽略html注入
    if (hljs) {
        hljs.configure({
            ignoreUnescapedHTML: true
        })
    }

}]);

app.controller('noteCtrl', ['$http', '$scope', function ($http, $scope) {

    //更新源
    setOrigin(ORIGINS[localStorage.getItem(CONSTANT.KEY_ORIGIN_SELECTED) || 0]);

    $http.get(absolutionPath(ORIGIN.CONFIG_PATH)).then((result) => {
        $scope.config = result.data;
        $scope.menus = $scope.config.menus;
        $scope.selectedItem = $scope.menus[0];

        //菜单选中记录
        let selectedRecord = localStorage.getItem(CONSTANT.KEY_MENU_SELECTED);

        //module加载
        menusForeach($scope.menus || [], menu => {
            if (menu.title === selectedRecord) {
                $scope.selectedItem = menu;
            }
            if (menu.type === CONSTANT.MENU_TYPE_MODULE && menu.modulePath !== undefined) {
                const moduleMenuPath = $scope.config.noteDir + menu.modulePath + CONSTANT.MODULE_MENU_PATH;
                $http.get(absolutionPath(moduleMenuPath)).then((result) => {
                    angular.extend(menu, result.data);
                    menu.filePath = menu.modulePath + menu.filePath;
                    menusForeach(menu.subMenus || [], subMenu => {
                        subMenu.filePath = menu.modulePath + subMenu.filePath;
                        if (subMenu.title === selectedRecord) {
                            $scope.selectedItem = subMenu;
                            $scope.notePath = notePath($scope.selectedItem.filePath);
                        }
                    });
                    //设置默认值
                    if ($scope.notePath === undefined && $scope.selectedItem === menu) {
                        $scope.notePath = notePath($scope.selectedItem.filePath);
                    }
                });
            } else {
                if ($scope.notePath === undefined && $scope.selectedItem === menu) {
                    $scope.notePath = notePath($scope.selectedItem.filePath);
                }
            }
        });

        $scope.moduleFilePath = function (relativePath) {
            return absolutionPath($scope.config.noteDir + $scope.selectedItem.filePath + "/.." + relativePath);
        };

        $scope.relativeFilePath = function (relativePath) {
            return absolutionPath($scope.config.noteDir + $scope.selectedItem.filePath + relativePath);
        };
    });

    //menus不为空
    function menusForeach(menus, consumer) {
        for (let i= 0; i < menus.length; i++) {
            consumer(menus[i]);
            menusForeach(menus[i].subMenus || [], consumer);
        }
    }

    //获取地址
    function absolutionPath (relativePath) {
        return ORIGIN.ROOT_PATH + relativePath;
    }

    //文件地址
    function notePath (filePath) {
        return absolutionPath($scope.config.noteDir + filePath);
    }

    $scope.select = function (item) {
        if ($scope.isSelected(item))
            return;
        //记录选中标题
        localStorage.setItem(CONSTANT.KEY_MENU_SELECTED, item.title);
        $scope.selectedItem = item;
        $scope.notePath = notePath($scope.selectedItem.filePath);
    };

    $scope.isSelected = function (item) {
        return item === $scope.selectedItem;
    };

    //数据源控制
    $scope.origins = angular.copy(ORIGINS, []);

    $scope.selectOrigin = function (index) {
        //记录选中的源
        localStorage.setItem(CONSTANT.KEY_ORIGIN_SELECTED, index);
        setOrigin(ORIGINS[index]);
        $http.get(absolutionPath(ORIGIN.CONFIG_PATH)).then((result) => {
            $scope.config = result.data;
            $scope.menus = $scope.config.menus;
            $scope.selectedItem = $scope.menus[0];

            //module加载
            menusForeach($scope.menus || [], menu => {
                if (menu.type === CONSTANT.MENU_TYPE_MODULE && menu.modulePath !== undefined) {
                    const moduleMenuPath = $scope.config.noteDir + menu.modulePath + CONSTANT.MODULE_MENU_PATH;
                    $http.get(absolutionPath(moduleMenuPath)).then((result) => {
                        angular.extend(menu, result.data);
                        menu.filePath = menu.modulePath + menu.filePath;
                        menusForeach(menu.subMenus || [], subMenu => {
                            subMenu.filePath = menu.modulePath + subMenu.filePath;
                        });
                        if ($scope.selectedItem === menu) {
                            $scope.notePath = notePath($scope.selectedItem.filePath);
                        }
                    });
                } else {
                    if ($scope.selectedItem === menu) {
                        $scope.notePath = notePath($scope.selectedItem.filePath);
                    }
                }
            });
        });
    }

    $scope.isSelectedOrigin = function (origin) {
        return origin.NAME === ORIGIN.NAME;
    };

    $scope.openSiderbar = false;

    document.onkeydown = function(e) {
        let keyCode = e.keyCode || e.which || e.charCode;
        let ctrlKey = e.ctrlKey || e.metaKey;
        //ctrl + c
        if(ctrlKey && keyCode === 67) {
            $scope.openSiderbar = !$scope.openSiderbar;
            $scope.$apply();
        }
    }

}]);

const menuItemTpl = `
<div>
    <div class="menu-item" ng-click="$ctrl.select($ctrl.itemData)" ng-class="{'active': $ctrl.itemData == $ctrl.selectedItem}">{{$ctrl.itemData.title}}</div>
    <div class="ml-3">
        <menu-item ng-repeat="subMenu in $ctrl.itemData.subMenus" item-data="subMenu" select-item="$ctrl.select(item)" selected-item="$ctrl.selectedItem"></menu-item>
    </div>
</div>
`;

app.component('menuItem', {
    template: menuItemTpl,
    bindings: {
        itemData: '=',
        selectItem: '&',
        selectedItem: '=',
    },
    controller: [function () {

        this.$onInit = function () {
        };

        this.$onChanges = function () {
        };

        this.select = function (item) {
            this.selectItem({item: item});
        }

    }]
});

const highlightTpl = `
<pre>
    <code></code>
</pre>`;

app.directive('highlight', ['$http', function ($http) {
    return {
        scope: {
        },
        restrict: 'EA',
        replace: true,
        template: highlightTpl,
        link: function (scope, element, attrs) {
            $http.get(attrs.highlight).then((result) => {
                element[0].children[0].innerHTML = result.data;
                if (hljs) {
                    hljs.highlightElement(element[0].children[0]);
                }
            });
        }
    };
}]);

const keyValueListTpl = `
<div class="table table-bordered" ng-transclude></div>
`;

app.directive('keyValueList', function () {
    return {
        scope: {},
        restrict: 'EA',
        replace: true,
        transclude: true,
        template: keyValueListTpl,
        link: function (scope, element, attrs) {
        }
    };
});

const keyValueItemTpl = `
<div class="d-flex">
    <div class="td w-25 font-weight-bold">{{itemKey}}</div>
    <div class="td w-75" ng-transclude></div>
</div>
`;

app.directive('keyValueItem', function () {
    return {
        scope: {
            itemKey: '@',
        },
        restrict: 'EA',
        replace: true,
        transclude: true,
        template: keyValueItemTpl,
        link: function (scope, element, attrs) {
        }
    };
});

const noteImageTpl = `
<div class="d-flex flex-column">
    <img class="m-auto" style="max-width: 70%;max-height: 70%;" ng-click="open()" ng-src="{{imageSrc}}">
    <em class="m-auto">{{imageLabel}}</em>
    <div class="image-modal d-flex flex-column">
        <div class="w-100 h-100 d-flex" ng-click="close()">
            <img class="m-auto" style="max-width: 100%;max-height: 100%;" ng-src="{{imageSrc}}">
        </div>
    </div>
</div>
`;

app.directive('noteImage', ['$document', function ($document) {
    return {
        scope: {
            'imageSrc': '@',
            'imageLabel': '@'
        },
        restrict: 'E',
        replace: true,
        template: noteImageTpl,
        link: function (scope, element, attrs) {

            let overlay = angular.element('<div>');
            let imageModal = angular.element(element.children()[2]);
            overlay.addClass("modal-overlay");

            scope.open = function () {
                overlay.bind('click', () => scope.close());
                $document[0].body.append(overlay[0]);
                imageModal.addClass('image-modal-show');
            }

            scope.close = function () {
                overlay.unbind('click');
                overlay.remove();
                imageModal.removeClass('image-modal-show');
            }
        }
    };
}]);

const noteTipTpl = `
<div class="d-flex flex-column">
    <div class="p-1 font-color-white" ng-class="style.labelBg">{{label}}</div>
    <div class="p-2 break-wrap" ng-class="style.contentBg" ng-transclude>
    </div>
</div>
`;

app.directive('noteTip', [function () {
    return {
        scope: {
            'label': '@',
            'level': '@'
        },
        restrict: 'E',
        replace: true,
        transclude: true,
        template: noteTipTpl,
        link: function (scope, element, attrs) {
            if (scope.level) {
                scope.style = {
                    labelBg: 'bg-' + scope.level,
                    contentBg: 'bg-' + scope.level + '-light',
                };
            } else {
                scope.style = {
                    labelBg: 'bg-primary',
                    contentBg: 'bg-primary-light',
                };
            }
        }
    };
}]);

const noteParagraphTpl = `
<div class="d-flex flex-column">
    <div class="mt-1 mb-1 font-color-primary font-weight-lighter" ng-class="{'font-size-5' : !isSecondary(), 'font-size-4' : isSecondary()}">{{label}}</div>
    <div class="divider"></div>
    <div class="mt-2 mb-2" ng-transclude>
    </div>
</div>
`;

app.directive('noteParagraph', [function () {
    return {
        scope: {
            'label': '@',
            'level': '@'
        },
        restrict: 'E',
        replace: true,
        transclude: true,
        template: noteParagraphTpl,
        link: function (scope, element, attrs) {

            scope.isSecondary = function() {
                if (scope.level && scope.level == "main") {
                    return false;
                }
                return true;
            }
        }
    };
}]);

const noteApiTpl = `
<div class="d-flex flex-column">
    <div class="mt-1 mb-1 mr-auto p-1 font-size-3 bg-secondary">{{method}}</div>
    <div class="mt-2 mb-2" ng-transclude>
    </div>
</div>
`;

app.directive('noteApi', [function () {
    return {
        scope: {
            'method': '@'
        },
        restrict: 'E',
        replace: true,
        transclude: true,
        template: noteApiTpl,
        link: function (scope, element, attrs) {

        }
    };
}]);