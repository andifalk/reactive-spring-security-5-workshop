// tagcloud
(function(){
    [].forEach.call( document.querySelectorAll('[tagcloud]'), function(cloud) {
        // Find all tagcloud items with a weight defined and add them to this array
        var weights = [].slice.call(cloud.querySelectorAll('[tagcloud-weight]'))
            .map(function(el){ return el.getAttribute('tagcloud-weight') })
            .sort(function(a, b){ return b-a }); // Sort descending

        var upperBound = weights[0];
        var lowerBound = weights[ weights.length - 1 ];
        var denominator = upperBound - lowerBound;
        var slideNotes = cloud.querySelectorAll('.notes');
        var isBlackWhite = cloud.hasAttribute('bw');

        /**
         * Parses the text, removing any notes and formats each node with a span if one
         * doesn't exist
         *
         * @param text {String} the text of the slide
         * @returns {String} the formatted slide content
         **/
        function formatTags(text) {
            for(index = 0; index < slideNotes.length; ++index) {
                text = text.replace(slideNotes[index].textContent, '');
            }
            //@see http://stackoverflow.com/questions/24512636/split-words-shuffle-jumble-letters
            var a = text.split(/\n/),
                n = a.length;

            if (cloud.hasAttribute('shuffle')) {
                //shuffle order
                for (var i = n - 1; i > 0; i--) {
                    var j = Math.floor(Math.random() * (i + 1));
                    var tmp = a[i];
                    a[i] = a[j];
                    a[j] = tmp;
                }
            } //end if shuffle

            return a.filter(function(item) {
                return item.trim() !== '';
            })
                .map(function(item) {
                    return ( item.indexOf('span') === -1 ) ? '<span>' + item.trim() + '</span> ' : item.trim();
                })
                .join("");

        }

        /**
         * Calculates the size of the element.
         * If one or more of the tags has a weight attribute, all sizes are based on weights.
         * If none of the elements have weights, the sizes are random.
         *
         * @param {DOM Element} the tag to calculate the size of.
         * @return {Number} the percentage to set the font size to
         **/
        function calcSize(elem) {
            var prctnge;

            // At least one of our cloud items is weighted, base sizes around weights
            if( weights.length > 0 ) {
                var itemWeight = elem.getAttribute('tagcloud-weight') || 0;
                var numerator = itemWeight - lowerBound;
                prctnge = (numerator / denominator) * 150 + 50;
            }
            // None of the cloud items are weighted, base the size randomly
            else {
                prctnge = Math.random() * 150 + 50;
            }

            if (cloud.hasAttribute('large')) {
                prctnge = prctnge * 1.2;
            }

            return prctnge;
        }

        /**
         * Applies a color to the tag.
         * If one or more tags have a weight attribute, colors are more intense
         * based on the weight. Otherwise colors, are chosen randomly.
         *
         * @param {DOM Element} the tag to color.
         **/
        function tagColor(elem, isBlackWhite) {
            var color;

            if (isBlackWhite) {
                var col = Math.round(Math.random() * 155 + 100);
                color = 'rgb('+ col  +',' + col + ',' + col + ')';
            } else {
                color = 'hsl('+ Math.random()*360 +', 40%, 50%)';
            }

            return color;
        }

        // Replace the inner html of the slide with the formatted tags
        cloud.innerHTML = formatTags(cloud.innerHTML);

        // Append the slideNotes to the slide again
        for(index = 0; index < slideNotes.length; ++index) {
            cloud.appendChild(slideNotes[index]);
        }

        // Size and colour the cloud tags
        [].forEach.call( cloud.querySelectorAll('span'), function(elem) {
            elem.style.fontSize = calcSize(elem) + '%';
            elem.classList.add('clouditem');
            if( elem.hasAttribute('tagcloud-link') ) {
                newelem = document.createElement('a');
                newelem.innerHTML = elem.innerHTML;
                newelem.style.color = tagColor(elem, isBlackWhite);
                newelem.setAttribute('href', '/#/' + elem.getAttribute('tagcloud-link'));
                elem.innerHTML = '';
                elem.appendChild(newelem);
            }
            else {
                elem.style.color = tagColor(elem, isBlackWhite);
            }
        });
    });
})();