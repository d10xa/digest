(function() {
    'use strict';

    angular
        .module('app')
        .directive('djtList', djtList);

    /* @ngInject */
    function djtList() {
        var directive = {
            restrict: 'E',
            templateUrl: '/list.html',
            scope: {
            },
            link: linkFunc,
            controller: ListController,
            controllerAs: 'vm',
            bindToController: true
        };

        return directive;

        function linkFunc(scope, el, attr, ctrl) {

        }
    }

    ListController.$inject = ['Digest'];

    /* @ngInject */
    function ListController(Digest) {
        var vm = this;

        activate();

        function activate() {
            vm.refresh = function (){
              Digest.getTasks().success(function(data){
                vm.tasks = data.tasks;
              });
            };
            vm.refresh();
        }
    }
})();
